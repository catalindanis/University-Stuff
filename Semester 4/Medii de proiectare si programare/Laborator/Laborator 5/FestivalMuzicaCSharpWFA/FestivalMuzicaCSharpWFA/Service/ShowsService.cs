using System.Configuration;
using FestivalMuzicaCSharp.Domain;
using FestivalMuzicaCSharp.Repository;
using FestivalMuzicaCSharpWFA.Utils;

namespace FestivalMuzicaCSharpWFA.Service;

public class ShowsService : IObservable {
    private static readonly ShowsService _instance = new  ShowsService();
    public static ShowsService Instance => _instance;
    private readonly IShowsRepository _showsRepository;
    private readonly ITicketsRepository _ticketsRepository;
    private List<IObserver> _observers;
    
    private ShowsService()
    {
        var props = new Dictionary<string, string>();
        var connStr = ConfigurationManager.ConnectionStrings["musicFestivalDB"]?.ConnectionString;
        if (connStr == null)
            throw new Exception("Cannot find connection string 'musicFestivalDB' in app.config");
        props["ConnectionString"] = connStr;
        
        _showsRepository = new ShowsDbRepository(props);
        _ticketsRepository = new TicketsDbRepository(props);
        
        _observers = new List<IObserver>();
    }

    public void Save(String artistName, DateOnly date, string location, int noSeats)
    {
        var show = new Show(0, artistName, date, location, noSeats);
        _showsRepository.Save(show);
        NotifyObservers();
    }
    
    public List<Show> FindAll()
    {
        return _showsRepository.FindAll().ToList();
    }

    public List<Show> FindAll(ShowFilter showFilter)
    {
        List<Show> shows = _showsRepository.FindAll().ToList();
        
        if(showFilter.ArtistName != null)
            shows = shows.Where(s => s.ArtistName.Contains(showFilter.ArtistName)).ToList();
        
        if(showFilter.Date != null)
            shows = shows.Where(s => s.Date.Equals(showFilter.Date)).ToList();
        
        if(showFilter.Location != null)
            shows = shows.Where(s => s.Location.Contains(showFilter.Location)).ToList();
        
        if(showFilter.RemainingSeats != null)
            shows = shows.Where(s => s.RemainingSeats.Equals(showFilter.RemainingSeats)).ToList();

        return shows;
    }
    
    public Show? FindOne(long id)
    {
        return _showsRepository.FindOne(id);
    }
    
    public void BookTicketForShow(long showId, string clientName, int noSeats)
    {
        var show = _showsRepository.FindOne(showId);
        if (show == null)
            throw new Exception("Show not found");
        
        if (show.RemainingSeats < noSeats)
            throw new Exception("Not enough seats available");

        var ticket = new Ticket(0, clientName, show, noSeats);
        show.RemainingSeats -= noSeats;

        _ticketsRepository.Save(ticket);
        _showsRepository.Update(showId, show);

        NotifyObservers();
    }
    
    public void UpdateTicket(long ticketId, string clientName, int noSeats)
    {
        var ticket = _ticketsRepository.FindOne(ticketId);
        if (ticket == null)
            throw new Exception("Ticket not found");
        
        var show = ticket.Show;
        int seatDifference = noSeats - ticket.NoSeats;

        if (show.RemainingSeats < seatDifference)
            throw new Exception("Not enough seats available");

        show.RemainingSeats -= seatDifference;
        ticket.ClientName = clientName;
        ticket.NoSeats = noSeats;

        _ticketsRepository.Update(ticketId, ticket);
        _showsRepository.Update(show.Id, show);

        NotifyObservers();
    }

    public List<Ticket> FindAllTickets()
    {
        return _ticketsRepository.FindAll().ToList();
    }
    
    public List<Ticket> FindAllTicketsForShow(long showId)
    {
        return _ticketsRepository.FindAll().Where(t => t.Show.Id == showId).ToList();
    }
    
    public int GetNumberOfSoldSeatsForShow(long showId)
    {
        return _ticketsRepository.FindAll().Where(t => t.Show.Id == showId).Sum(t => t.NoSeats);
    }
    
    public void Subscribe(IObserver observer)
    {
        if (!_observers.Contains(observer))
            _observers.Add(observer);
    }

    public void Unsubscribe(IObserver observer)
    {
        if (!_observers.Contains(observer))
            _observers.Remove(observer);
    }

    public void NotifyObservers()
    {
        foreach (var observer in _observers)
        {
            observer.Update();
        }
    }
}