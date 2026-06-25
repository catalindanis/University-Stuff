using networking.protocol;

namespace networking.dto;

[Serializable]
public class TicketResponseDTO : Response
{
    public long Id { get; set; }
    public string ClientName { get; set; }
    public ShowResponseDTO Show { get; set; }
    public int NoSeats { get; set; }

    public TicketResponseDTO() { }

    public TicketResponseDTO(long id, string clientName, ShowResponseDTO show, int noSeats)
    {
        Id = id;
        ClientName = clientName;
        Show = show;
        NoSeats = noSeats;
    }
}
