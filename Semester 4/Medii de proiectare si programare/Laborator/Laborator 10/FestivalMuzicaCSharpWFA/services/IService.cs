using FestivalMuzicaCSharp.Domain;
using services.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

public interface IService : IObservable
{
    User Login(string email, string password, IObserver client);

    void Logout(long userId, IObserver client);

    void Register(string email, string password);

    void Save(String artistName, DateOnly date, string location, int noSeats);

    List<Show> FindAll();

    List<Show> FindAll(ShowFilter showFilter);

    Show? FindOne(long id);

    void BookTicketForShow(long showId, string clientName, int noSeats);

    void UpdateTicket(long ticketId, long showId, string clientName, int noSeats);

    List<Ticket> FindAllTickets();

    List<Ticket> FindAllTicketsForShow(long showId);

    int GetNumberOfSoldSeatsForShow(long showId);
}
