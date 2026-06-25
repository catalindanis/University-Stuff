namespace FestivalMuzicaCSharp.Domain;

public class Ticket : IEntity<long>
{
    public long Id { get; set; }
    public string ClientName { get; set; }
    public Show Show { get; set; }
    public int NoSeats { get; set; }
    
    public Ticket(long id, string clientName, Show show, int noSeats)
    {
        Id = id;
        ClientName = clientName;
        Show = show;
        NoSeats = noSeats;
    }
    
    public override bool Equals(object? obj) => Equals(obj as Ticket);
    
    public bool Equals(Ticket? other)
    {
        if (other is null) return false;
        if (ReferenceEquals(this, other)) return true;

        return Id == other.Id && string.Equals(ClientName, other.ClientName, StringComparison.Ordinal)
                              && Show.Equals(other.Show)
                              && NoSeats == other.NoSeats;
    }
    
    public override int GetHashCode() => HashCode.Combine(Id, ClientName, Show, NoSeats);
    
    public override string ToString()
    {
        return $"Ticket{{id={Id}, clientName='{ClientName}', show={Show}, noSeats={NoSeats}}}";
    }
}