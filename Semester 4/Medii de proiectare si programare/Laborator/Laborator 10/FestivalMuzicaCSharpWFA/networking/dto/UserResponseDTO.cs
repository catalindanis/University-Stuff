using networking.protocol;

namespace networking.dto;

[Serializable]
public class UserResponseDTO : Response
{
    public long Id { get; set; }

    public UserResponseDTO() { }

    public UserResponseDTO(long id)
    {
        Id = id;
    }
}
