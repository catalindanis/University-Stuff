using networking.protocol;

namespace networking.dto;

[Serializable]
public class ShowResponseDTO : Response
{
    public long Id { get; set; }
    public string ArtistName { get; set; }
    public DateTime Date { get; set; }
    public string Location { get; set; }
    public int RemainingSeats { get; set; }

    public ShowResponseDTO() { }

    public ShowResponseDTO(long id, string artistName, DateTime date, string location, int remainingSeats)
    {
        Id = id;
        ArtistName = artistName;
        Date = date;
        Location = location;
        RemainingSeats = remainingSeats;
    }
}
