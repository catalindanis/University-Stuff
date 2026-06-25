using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharp.Repository;
using log4net;
using services.Utils;
using System.Configuration;
using System.Security.Cryptography;
using System.Text;


namespace server;
public class ServiceImpl : IService
{
    private IUsersRepository usersRepository;
    private IShowsRepository showsRepository;
    private ITicketsRepository ticketsRepository;
    private readonly IDictionary<String, IObserver> loggedClients;
    private static readonly ILog log = LogManager.GetLogger(typeof(ServiceImpl));
    private readonly string _encryptionAESKey;

    public ServiceImpl(IUsersRepository usersRepository, IShowsRepository showsRepository, ITicketsRepository ticketsRepository)
    {
        this.usersRepository = usersRepository;
        this.showsRepository = showsRepository;
        this.ticketsRepository = ticketsRepository;
        loggedClients = new Dictionary<String, IObserver>();
        _encryptionAESKey = ConfigurationManager.AppSettings["encryptionAESKey"];
        if (string.IsNullOrEmpty(_encryptionAESKey))
            throw new Exception("Missing encryptionAESKey in app.config <appSettings>");
    }

    public void BookTicketForShow(long showId, string clientName, int noSeats)
    {
        var show = showsRepository.FindOne(showId);
        if (show == null)
            throw new Exception("Show not found");

        if (show.RemainingSeats < noSeats)
            throw new Exception("Not enough seats available");

        var ticket = new Ticket(0, clientName, show, noSeats);
        show.RemainingSeats -= noSeats;

        ticketsRepository.Save(ticket);
        showsRepository.Update(showId, show);

        NotifyObservers();
    }

    public List<Show> FindAll()
    {
        return [.. showsRepository.FindAll()];
    }

    public List<Show> FindAll(ShowFilter showFilter)
    {
        List<Show> shows = [.. showsRepository.FindAll()];

        if (showFilter?.ArtistName != null)
            shows = shows.Where(s => s.ArtistName.Contains(showFilter.ArtistName)).ToList();

        if (showFilter?.Date != null)
            shows = shows.Where(s => s.Date.Equals(showFilter.Date)).ToList();

        if (showFilter?.Location != null)
            shows = shows.Where(s => s.Location.Contains(showFilter.Location)).ToList();

        if (showFilter?.RemainingSeats != null)
            shows = shows.Where(s => s.RemainingSeats.Equals(showFilter.RemainingSeats)).ToList();

        return shows;
    }

    public List<Ticket> FindAllTickets()
    {
        return [.. ticketsRepository.FindAll()];
    }

    public List<Ticket> FindAllTicketsForShow(long showId)
    {
        return [.. ticketsRepository.FindAll().Where(t => t.Show.Id == showId)];
    }

    public Show? FindOne(long id)
    {
        return showsRepository.FindOne(id);
    }

    public int GetNumberOfSoldSeatsForShow(long showId)
    {
        return ticketsRepository.FindAll().Where(t => t.Show.Id == showId).Sum(t => t.NoSeats);
    }

    public void Save(string artistName, DateOnly date, string location, int noSeats)
    {
        var show = new Show(0, artistName, date, location, noSeats);
        showsRepository.Save(show);
        NotifyObservers();
    }

    public void UpdateTicket(long ticketId, long showId, string clientName, int noSeats)
    {
        var ticket = ticketsRepository.FindOne(ticketId) ?? throw new Exception("Ticket not found");

        var show = ticket.Show;
        int seatDifference = noSeats - ticket.NoSeats;

        if (show.RemainingSeats < seatDifference)
            throw new Exception("Not enough seats available");

        show.RemainingSeats -= seatDifference;
        ticket.ClientName = clientName;
        ticket.NoSeats = noSeats;

        ticketsRepository.Update(ticketId, ticket);
        showsRepository.Update(show.Id, show);

        NotifyObservers();
    }

    public void NotifyObservers()
    {
        //Console.WriteLine("~~~~~~~~Notific");
        foreach (var item in loggedClients)
        {
            Task.Run(() =>
            {
                //Console.WriteLine("item");
                item.Value.Update();
            });
        }
    }

    public User Login(string email, string password, IObserver client)
    {
        string encryptedPassword = Encrypt(password, _encryptionAESKey);

        var user = usersRepository.FindByEmailAndPassword(email, encryptedPassword);
        if (user != null)
        {
            if (loggedClients.ContainsKey(user.Id.ToString()))
                throw new Exception("User already logged in");
            loggedClients[user.Id.ToString()] = client;
            return user;
        }

        throw new Exception("Invalid credentials");
    }

    public void Logout(long userId, IObserver client)
    {
        _ = loggedClients[userId.ToString()] ?? throw new Exception("User " + userId + " is not logged in.");
        loggedClients.Remove(userId.ToString());
    }

    public void Register(string email, string password)
    {
        string encryptedPassword = Encrypt(password, _encryptionAESKey);

        var existingUser = usersRepository.FindByEmail(email);
        if (existingUser != null)
            throw new Exception("User already exists with this email");

        usersRepository.Save(new User(0, email, encryptedPassword));
    }

    public string Encrypt(string data, string key)
    {
        try
        {
            using Aes aesAlg = Aes.Create();
            aesAlg.Key = Encoding.UTF8.GetBytes(key.PadRight(32).Substring(0, 32));
            aesAlg.Mode = CipherMode.ECB;
            aesAlg.Padding = PaddingMode.PKCS7;
            using ICryptoTransform encryptor = aesAlg.CreateEncryptor();
            byte[] inputBytes = Encoding.UTF8.GetBytes(data);
            byte[] encrypted = encryptor.TransformFinalBlock(inputBytes, 0, inputBytes.Length);
            return Convert.ToBase64String(encrypted);
        }
        catch
        {
            throw new Exception("Password encryption failed!");
        }
    }

    public void Subscribe(IObserver observer)
    {
        throw new NotImplementedException();
    }

    public void Unsubscribe(IObserver observer)
    {
        throw new NotImplementedException();
    }
}
