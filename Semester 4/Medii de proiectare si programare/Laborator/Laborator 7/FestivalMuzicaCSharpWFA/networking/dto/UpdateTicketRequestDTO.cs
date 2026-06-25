using networking.protocol;

namespace networking.dto;

[Serializable]
public class UpdateTicketRequestDTO : Request
{
    public long TicketId { get; set; }
    public long ShowId { get; set; }
    public string Client { get; set; }
    public int NumberOfSeats { get; set; }

    public UpdateTicketRequestDTO() { }

    public UpdateTicketRequestDTO(long ticketId, long showId, string client, int numberOfSeats)
    {
        TicketId = ticketId;
        ShowId = showId;
        Client = client;
        NumberOfSeats = numberOfSeats;
    }
}
