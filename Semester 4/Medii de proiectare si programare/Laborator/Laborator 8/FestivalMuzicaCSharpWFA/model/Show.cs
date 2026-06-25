namespace FestivalMuzicaCSharp.Domain;

public class Show : IEntity<long>
{
    public long Id { get; set; }
    public string ArtistName { get; set; }
    public DateOnly Date { get; set; }
    public string Location { get; set; }
    public int RemainingSeats { get; set; }
    
    public Show(long id, string artistName, DateOnly date, string location, int remainingSeats)
    {
        Id = id;
        ArtistName = artistName;
        Date = date;
        Location = location;
        RemainingSeats = remainingSeats;
    }

    public override bool Equals(object? obj) => Equals(obj as Show);

    public bool Equals(Show? other)
    {
        if (other is null) return false;
        if (ReferenceEquals(this, other)) return true;

        return Id == other.Id && string.Equals(ArtistName, other.ArtistName, StringComparison.Ordinal)
                              && string.Equals(Location, other.Location, StringComparison.Ordinal)
                              && Date.Equals(other.Date)
                              && RemainingSeats == other.RemainingSeats;
    }
    
    public override int GetHashCode() => HashCode.Combine(Id, ArtistName, Date, Location, RemainingSeats);

    public override string ToString()
    {
        return $"Show{{id={Id}, artistName='{ArtistName}', date={Date}, location='{Location}', remainingSeats={RemainingSeats}}}";
    }
}