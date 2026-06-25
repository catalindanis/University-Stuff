using networking.protocol;

namespace networking.dto;

[Serializable]
public class ShowRequestDTO : Request
{
    public string ArtistName { get; set; }
    public DateTime Date { get; set; }
    public string Location { get; set; }
    public int NumberOfSeats { get; set; }

    public ShowRequestDTO() { }

    public ShowRequestDTO(string artistName, DateTime date, string location, int numberOfSeats)
    {
        ArtistName = artistName;
        Date = date;
        Location = location;
        NumberOfSeats = numberOfSeats;
    }
}
