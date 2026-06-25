using services.Utils;
using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using FestivalMuzicaCSharp.Domain;
using networking.dto;
using System.Text.Json;
using System.Text;

namespace networking.protocol;

public class ClientWorker : IObserver 
{
	private IService server;
	private TcpClient connection;

	private NetworkStream stream;
    private volatile bool connected;
	public ClientWorker(IService server, TcpClient connection)
	{
		this.server = server;
		this.connection = connection;
		try
		{
			stream = connection.GetStream();
            connected = true;
		}
		catch (Exception e)
		{
            Console.WriteLine(e.StackTrace);
		}
	}

	public virtual void run()
	{
        using StreamReader reader = new StreamReader(stream, Encoding.UTF8);
		while(connected)
		{
			try
			{
                string requestJson = reader.ReadLine();

                if (string.IsNullOrEmpty(requestJson))
                    continue;

                //object request = formatter.Deserialize(stream);
                Request request = JsonSerializer.Deserialize<Request>(requestJson);
				object response = handleRequest(request);
				if (response != null)
				{
				   sendResponse((Response) response);
				}
			}
			catch (Exception e)
			{
                Console.WriteLine(e.StackTrace);
			}
		}

		try
		{
			stream.Close();
			connection.Close();
			connected = false;
		}
		catch (Exception e)
		{
			Console.WriteLine("Error "+e);
		}
	}

	private Response handleRequest(Request request)
	{
		Response response = null;

		if (request is LoginRequest)
		{
			Console.WriteLine("Login request ...");
			LoginRequest logReq = (LoginRequest) request;
			UserRequestDTO udto = logReq.User;
			try
            {
                lock (server)
				{
                    User user = server.Login(udto.Email, udto.Password, this);
                    return new UserResponseDTO(user.Id);
                }
            }
            catch (Exception e)
			{
				return new ErrorResponse(e.Message);
			}
		}

        if (request is RegisterRequest)
        {
            Console.WriteLine("Register request");
            RegisterRequest logReq = (RegisterRequest) request;
            UserRequestDTO udto = logReq.User;
            try
            {
                lock (server)
                {
                    server.Register(udto.Email, udto.Password);
                }
                return new OkResponse();
            }
            catch (Exception e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is LogoutRequest)
		{
			Console.WriteLine("Logout request");
			LogoutRequest logReq = (LogoutRequest) request;
			UserRequestDTO udto = logReq.User;
			try
			{
                lock (server)
                {
                    server.Logout(udto.Id, this);
                }
                connected = false;
                return new OkResponse();
			}
			catch (Exception e)
			{
                return new ErrorResponse(e.Message);
			}
		}

		if(request is SaveShowRequest saveShowRequest)
		{
            Console.WriteLine("Save show request");
            ShowRequestDTO sdto = saveShowRequest.Show;
            try
            {
                lock (server)
                {
                    server.Save(sdto.ArtistName, DateOnly.FromDateTime(sdto.Date), sdto.Location, sdto.NumberOfSeats);
                }
                return new OkResponse();
            }
            catch (Exception e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is FindAllShowsRequest findAllShowsRequest)
        {
            Console.WriteLine("Find all shows request");
            ShowFilter showFilter = findAllShowsRequest.ShowFilter;
            try
            {
                List<ShowResponseDTO> shows;
                lock (server)
                {
                    shows = server.FindAll(showFilter).Select(show => new ShowResponseDTO(
                        show.Id,
                        show.ArtistName,
                        show.Date.ToDateTime(TimeOnly.MinValue),
                        show.Location,
                        show.RemainingSeats
                    )).ToList();
                }
                OkResponse resp = new OkResponse();
                if (shows != null)
                    resp.AddData("shows", shows);
                return resp;
            }
            catch (Exception e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is FindShowRequest findShowRequest)
        {
            Console.WriteLine("Find show request");
            try
            {
                Show show = null;
                lock (server)
                {
                    show = server.FindOne(findShowRequest.Id);
                }
                connected = false;
                OkResponse resp = new OkResponse();
                if (show != null)
                    resp.AddData("show", show);
                return resp;
            }
            catch (Exception e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is BookTicketRequest bookTicketRequest)
        {
            Console.WriteLine("Book ticket request");
            try
            {
                BookTicketRequestDTO bookTicketDTO = bookTicketRequest.BookTicketDTO;
                lock (server)
                {
                    server.BookTicketForShow(
                        bookTicketDTO.ShowId,
                        bookTicketDTO.Client,
                        bookTicketDTO.NumberOfSeats
                    );
                }
                return new OkResponse();
            }
            catch (Exception e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if(request is UpdateTicketRequest updateTicketRequest)
        {
            Console.WriteLine("Update ticket request");
            try
            {
                UpdateTicketRequestDTO updateTicketDTO = updateTicketRequest.UpdateTicketRequestDTO;
                lock (server)
                {
                    server.UpdateTicket(
                        updateTicketDTO.TicketId,
                        updateTicketDTO.ShowId,
                        updateTicketDTO.Client,
                        updateTicketDTO.NumberOfSeats
                    );
                }
                return new OkResponse();
            }
            catch (Exception e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is FindAllTicketsRequest findAllTicketsRequest)
        {
            Console.WriteLine("Find all tickets request");
            try
            {
                List<TicketResponseDTO> tickets = null;
                lock (server)
                {
                    tickets = server.FindAllTickets().Select(ticket => new TicketResponseDTO(
                       ticket.Id,
                       ticket.ClientName,
                       new ShowResponseDTO(
                            ticket.Show.Id,
                            ticket.Show.ArtistName,
                            ticket.Show.Date.ToDateTime(TimeOnly.MinValue),
                            ticket.Show.Location,
                            ticket.Show.RemainingSeats
                       ),
                       ticket.NoSeats
                    )).ToList();
                }
                OkResponse resp = new OkResponse();
                resp.AddData("tickets", tickets);
                return resp;
            }
            catch (Exception e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        if (request is FindTicketsForShowRequest findTicketsForShowRequest)
        {
            Console.WriteLine("Find tickets for show request");
            try
            {
                List<TicketResponseDTO> tickets = null;
                lock (server)
                {
                    tickets = server.FindAllTicketsForShow(findTicketsForShowRequest.ShowId).Select(ticket => new TicketResponseDTO(
                       ticket.Id,
                       ticket.ClientName,
                       new ShowResponseDTO(
                            ticket.Show.Id,
                            ticket.Show.ArtistName,
                            ticket.Show.Date.ToDateTime(TimeOnly.MinValue),
                            ticket.Show.Location,
                            ticket.Show.RemainingSeats
                       ),
                       ticket.NoSeats
                    )).ToList();
                }
                OkResponse resp = new OkResponse();
                resp.AddData("tickets", tickets);
                return resp;
            }
            catch (Exception e)
            {
                return new ErrorResponse(e.Message);
            }
        }

        return response;
	}

	private void sendResponse(Response response)
	{
        try
        {
            string jsonString = JsonSerializer.Serialize(response);
            Console.WriteLine("sending response " + response);

            lock (stream)
            {
                byte[] data = Encoding.UTF8.GetBytes(jsonString + "\n");
                //formatter.Serialize(stream, response);
                stream.Write(data, 0, data.Length);
                stream.Flush();
            }
        }
        catch (Exception e)
        {
            
        }
	}

    public void Update()
    {
        sendResponse(new UpdateResponse());
    }
}

