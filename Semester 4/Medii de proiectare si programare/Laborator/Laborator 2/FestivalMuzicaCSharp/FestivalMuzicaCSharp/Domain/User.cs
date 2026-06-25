namespace FestivalMuzicaCSharp.Domain;

public class User : IEntity<long>, IEquatable<User>
{
    public long Id { get; set; }
    public string Email { get; set; }
    public string Password { get; set; }

    public User(long id, string email, string password)
    {
        Id = id;
        Email = email;
        Password = password;
    }

    public bool Equals(User? other)
    {
        if (other is null) return false;
        if (ReferenceEquals(this, other)) return true;

        return Id == other.Id && string.Equals(Email, other.Email, StringComparison.Ordinal)
                              && string.Equals(Password, other.Password, StringComparison.Ordinal);
    }

    public override bool Equals(object? obj) => Equals(obj as User);

    public override int GetHashCode() => HashCode.Combine(Id, Email, Password);

    public override string ToString()
    {
        return $"User{{id={Id}, email='{Email}', password='{Password}'}}";
    }
}