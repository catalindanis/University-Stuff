using FestivalMuzicaCSharp;
using FestivalMuzicaCSharp.Domain;
using log4net;
using services.Utils;

namespace FestivalMuzicaClient;
public class Controller : IObserver, IObservable
{
    private readonly IService _service;
    private long _userId;
    private readonly List<IObserver> observers;
    private static readonly ILog log = LogManager.GetLogger(typeof(Controller));

    public Controller(IService service)
    {
        this._service = service;
        observers = new List<IObserver>();
    }

    public User Login(string email, string password)
    {
        User user = _service.Login(email, password, this);
        _userId = user.Id;
        return user;
    }

    public void Register(string email, string password)
    {
        _service.Register(email, password);
    }

    public void Logout()
    {
        _service.Logout(_userId, this);
    }

    public void BookTicketForShow(long showId, String client, int numberOfSeats)
    {
        _service.BookTicketForShow(showId, client, numberOfSeats);
    }

    public List<Show> FindAll()
    {
        return _service.FindAll();
    }

    public List<Show> FindAll(ShowFilter showFilter)
    {
        return _service.FindAll(showFilter);
    }

    public int GetNumberOfSoldSeatsForShow(long showId)
    {
        return _service.GetNumberOfSoldSeatsForShow(showId);
    }

    public void Update()
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
        log.Error("[CATALIN] Am primit un update request");
        foreach (var observer in observers)
        {
            observer.Update();
            log.Error("[CATALIN] " + observer);
        }
    }

    public List<Ticket> FindAllTickets()
    {
        return _service.FindAllTickets();
    }

    public void UpdateTicket(long id, long showId, string clientName, int numberOfSeats)
    {
        _service.UpdateTicket(id, showId, clientName, numberOfSeats);
    }
}
