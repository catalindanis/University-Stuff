using networking.protocol;

namespace networking.dto;

[Serializable]
public class BookTicketRequestDTO : Request
{
    public long ShowId { get; set; }
    public string Client { get; set; }
    public int NumberOfSeats { get; set; }

    public BookTicketRequestDTO() { }

    public BookTicketRequestDTO(long showId, string client,  int numberOfSeats)
    {
        ShowId = showId;
        Client = client;
        NumberOfSeats = numberOfSeats;
    }
}
