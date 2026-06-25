using networking.protocol;

namespace networking.dto;

[Serializable]
public class UserRequestDTO : Request
{
    public long Id { get; set; }
    public string Email { get; set; }
    public string Password { get; set; }

    public UserRequestDTO() { }

    public UserRequestDTO(long id, string email, string password)
    {
        Id = id;
        Email = email;
        Password = password;
    }
}