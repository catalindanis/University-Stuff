using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharp.Repository;
using log4net;
using networking.dto;
using services.Utils;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Text.Json;

namespace networking.protocol;

public class ServerProxy : IService
{
	private string host;
	private int port;

	private IObserver client;

	private NetworkStream stream;

    private TcpClient connection;

	private Queue<Response> responses;
	private volatile bool finished;
    private EventWaitHandle _waitHandle;
    private readonly ILog Log = LogManager.GetLogger(typeof(ServerProxy));

    private readonly List<IObserver> observers;

    public ServerProxy(string host, int port)
	{
		this.host = host;
		this.port = port;
		responses=new Queue<Response>();
        observers = new List<IObserver>();
	}

	private void closeConnection()
	{
		finished = true;
		try
		{
			stream.Close();
			connection.Close();
            _waitHandle.Close();
			client = null;
		}
		catch (Exception e)
		{
			Console.WriteLine(e.StackTrace);
		}

	}
	private void sendRequest(Request request)
	{
		try
		{
			lock (stream)
			{
                string jsonRequest = JsonSerializer.Serialize(request);
                byte[] data = Encoding.UTF8.GetBytes(jsonRequest + "\n");
                Log.Error("[CATALIN] Trimit request" + request);
                stream.Write(data, 0, data.Length);
				//formatter.Serialize(stream, request);
				stream.Flush();
			}
		}
		catch (Exception e)
		{
            Log.Error(e);
			throw new Exception("Error sending object " + e.ToString());
		}

	}
	private Response readResponse()
	{
		Response response = null;
		try
		{
            _waitHandle.WaitOne();
			lock (responses)
			{
                //Monitor.Wait(responses); 
                response = responses.Dequeue();
			}
		}
		catch (Exception e)
		{
			Console.WriteLine(e.StackTrace);
		}
		return response;
	}
	private void initializeConnection()
	{
		 try
		 {
			connection=new TcpClient(host,port);
			stream=connection.GetStream();
            finished =false;
            _waitHandle = new AutoResetEvent(false);
			startReader();
		}
		catch (Exception e)
		{
            Console.WriteLine(e.StackTrace);
		}
	}
	private void startReader()
	{
		Thread tw = new Thread(run);
		tw.Start();
	}
	public virtual void run()
	{
        using StreamReader reader = new StreamReader(stream);
		while(!finished)
		{
			try
			{
                string responseJson = reader.ReadLine();
                if (string.IsNullOrEmpty(responseJson))
                    continue;

                //object response = formatter.Deserialize(stream);
                Response response = JsonSerializer.Deserialize<Response>(responseJson);

				Log.Error("response received "+response);
                if (response is UpdateResponse)
                {
                    handleUpdate((UpdateResponse) response);
                }
                else
                {
                    lock (responses)
					{
                        responses.Enqueue((Response) response);
                       
					}
                    _waitHandle.Set();
                }
            }
			catch (Exception e)
			{
				Console.WriteLine("Reading error "+e);
			}
		}
	}

    public User Login(string email, string password, IObserver client)
    {
        initializeConnection();
        UserRequestDTO udto = new UserRequestDTO(-1, email, password);
        sendRequest(new LoginRequest(udto));
        Response response = readResponse();

        if (response is UserResponseDTO userResponseDTO)
        {
            this.client = client;
            return new User(userResponseDTO.Id, "", "");
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse) response;
            throw new Exception(err.Message);
        }

		return null;
    }

    public void Logout(long userId, IObserver client)
    {
        UserRequestDTO udto = new UserRequestDTO(userId, "", "");
        sendRequest(new LogoutRequest(udto));
        Response response = readResponse();

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse) response;
            throw new Exception(err.Message);
        }
        else
            closeConnection();
    }

    public void Register(string email, string password)
    {
        UserRequestDTO udto = new UserRequestDTO(-1, email, password);
        sendRequest(new RegisterRequest(udto));
        Response response = readResponse();

        if (response is UserResponseDTO userResponseDTO)
        {
			return;
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse) response;
            throw new Exception(err.Message);
        }
    }

    public void Save(string artistName, DateOnly date, string location, int noSeats)
    {
        ShowRequestDTO sdto = new ShowRequestDTO(artistName, date.ToDateTime(TimeOnly.MinValue), location, noSeats);
        sendRequest(new SaveShowRequest(sdto));
        Response response = readResponse();

        if (response is OkResponse)
        {
            return;
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse) response;
            throw new Exception(err.Message);
        }
    }

    public List<Show> FindAll()
    {
        sendRequest(new FindAllShowsRequest());
        Response response = readResponse();

        if (response is OkResponse okResponse)
        {
            Dictionary<string, object> data = okResponse.Data;
            List<ShowResponseDTO> showsResponseDTO = ((JsonElement)data["shows"]).Deserialize<List<ShowResponseDTO>>(new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            return showsResponseDTO.Select(dto => new Show(
                dto.Id,
                dto.ArtistName,
                DateOnly.FromDateTime(dto.Date),
                dto.Location,
                dto.RemainingSeats
            )).ToList();
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse)response;
            throw new Exception(err.Message);
        }

        return null;
    }

    public List<Show> FindAll(ShowFilter showFilter)
    {
        sendRequest(new FindAllShowsRequest(showFilter));
        Response response = readResponse();

        if (response is OkResponse okResponse)
        {
            Dictionary<string, object> data = okResponse.Data;
            List<ShowResponseDTO> showsResponseDTO = ((JsonElement)data["shows"]).Deserialize<List<ShowResponseDTO>>(new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            return showsResponseDTO.Select(dto => new Show(
                dto.Id,
                dto.ArtistName,
                DateOnly.FromDateTime(dto.Date),
                dto.Location,
                dto.RemainingSeats
            )).ToList();
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse)response;
            throw new Exception(err.Message);
        }

        return null;
    }

    public Show? FindOne(long id)
    {
        sendRequest(new FindShowRequest(id));
        Response response = readResponse();

        if (response is OkResponse okResponse)
        {
            Dictionary<string, object> data = okResponse.Data;
            ShowResponseDTO showResponseDTO = ((JsonElement)data["show"]).Deserialize<ShowResponseDTO>(new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

            return new Show(
                showResponseDTO.Id,
                showResponseDTO.ArtistName,
                DateOnly.FromDateTime(showResponseDTO.Date),
                showResponseDTO.Location,
                showResponseDTO.RemainingSeats
            );
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse) response;
            throw new Exception(err.Message);
        }

        return null;
    }

    public void BookTicketForShow(long showId, string clientName, int noSeats)
    {
        BookTicketRequestDTO bookTicketDTO = new BookTicketRequestDTO(showId, clientName, noSeats);
        sendRequest(new BookTicketRequest(bookTicketDTO));
        Response response = readResponse();

        if (response is OkResponse okResponse)
        {
            return;
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse)response;
            throw new Exception(err.Message);
        }
    }

    public void UpdateTicket(long ticketId, long showId, string clientName, int noSeats)
    {
        UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO(ticketId, showId, clientName, noSeats);
        sendRequest(new UpdateTicketRequest(updateTicketRequestDTO));
        Response response = readResponse();

        if (response is OkResponse okResponse)
        {
            return;
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse) response;
            throw new Exception(err.Message);
        }
    }

    public List<Ticket> FindAllTickets()
    {
        sendRequest(new FindAllTicketsRequest());
        Response response = readResponse();

        if (response is OkResponse okResponse)
        {
            Dictionary<string, object> data = okResponse.Data;
            List<TicketResponseDTO> ticketsResponseDTO = ((JsonElement)data["tickets"]).Deserialize<List<TicketResponseDTO>>(new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            return ticketsResponseDTO.Select(dto => new Ticket(
                dto.Id,
                dto.ClientName,
                new Show(
                    dto.Show.Id,
                    dto.Show.ArtistName,
                    DateOnly.FromDateTime(dto.Show.Date),
                    dto.Show.Location,
                    dto.Show.RemainingSeats
                ),
                dto.NoSeats
            )).ToList();
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse)response;
            throw new Exception(err.Message);
        }

        return null;
    }

    public List<Ticket> FindAllTicketsForShow(long showId)
    {
        sendRequest(new FindTicketsForShowRequest(showId));
        Response response = readResponse();

        if (response is OkResponse okResponse)
        {
            Dictionary<string, object> data = okResponse.Data;
            List<TicketResponseDTO> ticketsResponseDTO = ((JsonElement)data["tickets"]).Deserialize<List<TicketResponseDTO>>(new JsonSerializerOptions { PropertyNameCaseInsensitive = true });
            return ticketsResponseDTO.Select(dto => new Ticket(
                dto.Id,
                dto.ClientName,
                new Show(
                    dto.Show.Id,
                    dto.Show.ArtistName,
                    DateOnly.FromDateTime(dto.Show.Date),
                    dto.Show.Location,
                    dto.Show.RemainingSeats
                ),
                dto.NoSeats
            )).ToList();
        }

        if (response is ErrorResponse)
        {
            ErrorResponse err = (ErrorResponse) response;
            throw new Exception(err.Message);
        }

        return null;
    }

    public int GetNumberOfSoldSeatsForShow(long showId)
    {
        return FindAllTicketsForShow(showId).Sum(ticket => ticket.NoSeats);
    }

    private void handleUpdate(UpdateResponse response)
    {
        NotifyObservers();
    }

    public void Subscribe(IObserver observer)
    {
        observers.Add(observer);
    }

    public void Unsubscribe(IObserver observer)
    {
        observers.Remove(observer);
    }

    public void NotifyObservers()
    {
        this.client.Update();
        //foreach (var observer in observers)
        //    observer.Update();
    }
}
