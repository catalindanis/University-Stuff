using FestivalMuzicaCSharp.Domain;
using networking.dto;
using System.Text.Json.Serialization;

namespace networking.protocol;

[JsonPolymorphic(TypeDiscriminatorPropertyName = "$type")]
[JsonDerivedType(typeof(LoginRequest), "LoginRequest")]
[JsonDerivedType(typeof(RegisterRequest), "RegisterRequest")]
[JsonDerivedType(typeof(LogoutRequest), "LogoutRequest")]
[JsonDerivedType(typeof(BookTicketRequest), "BookTicketRequest")]
[JsonDerivedType(typeof(FindAllShowsRequest), "FindAllShowsRequest")]
[JsonDerivedType(typeof(FindAllTicketsRequest), "FindAllTicketsRequest")]
[JsonDerivedType(typeof(FindShowRequest), "FindShowRequest")]
[JsonDerivedType(typeof(FindTicketsForShowRequest), "FindTicketsForShowRequest")]
[JsonDerivedType(typeof(SaveShowRequest), "SaveShowRequest")]
[JsonDerivedType(typeof(UpdateTicketRequest), "UpdateTicketRequest")]
[JsonDerivedType(typeof(UserRequestDTO), "UserRequestDTO")]
[JsonDerivedType(typeof(ShowRequestDTO), "ShowRequestDTO")]
[JsonDerivedType(typeof(BookTicketRequestDTO), "BookTicketRequestDTO")]
[JsonDerivedType(typeof(UpdateTicketRequestDTO), "UpdateTicketRequestDTO")]
public class Request
{
}

[Serializable]
public class LoginRequest : Request
{
    private UserRequestDTO user;

    [JsonConstructor]
    public LoginRequest(UserRequestDTO user)
    {
        this.user = user;
    }

    public virtual UserRequestDTO User
    {
        get
        {
            return user;
        }
    }
}

[Serializable]
public class RegisterRequest : Request
{
    private UserRequestDTO user;

    [JsonConstructor]
    public RegisterRequest(UserRequestDTO user)
    {
        this.user = user;
    }

    public virtual UserRequestDTO User
    {
        get
        {
            return user;
        }
    }
}

[Serializable]
public class LogoutRequest : Request
{
    private UserRequestDTO user;

    [JsonConstructor]
    public LogoutRequest(UserRequestDTO user)
    {
        this.user = user;
    }

    public virtual UserRequestDTO User
    {
        get
        {
            return user;
        }
    }
}

[Serializable]
public class BookTicketRequest : Request
{
    public BookTicketRequestDTO BookTicketDTO { get; }

    [JsonConstructor]
    public BookTicketRequest(BookTicketRequestDTO bookTicketDTO)
    {
        BookTicketDTO = bookTicketDTO;
    }
}

[Serializable]
public class FindAllShowsRequest : Request
{
    public ShowFilter ShowFilter { get; }
    
    [JsonConstructor]
    public FindAllShowsRequest(ShowFilter showFilter)
    {
        ShowFilter = showFilter;
    }

    public FindAllShowsRequest()
    {

    }
}

[Serializable]
public class FindAllTicketsRequest : Request
{
}

[Serializable]
public class FindShowRequest : Request
{
    public long Id { get; }

    [JsonConstructor]
    public FindShowRequest(long id)
    {
        Id = id;
    }
}

[Serializable]
public class FindTicketsForShowRequest : Request
{
    public long ShowId { get; }

    [JsonConstructor]
    public FindTicketsForShowRequest(long showId)
    {
        ShowId = showId;
    }
}

[Serializable]
public class SaveShowRequest : Request
{
    public ShowRequestDTO Show { get; }

    [JsonConstructor]
    public SaveShowRequest(ShowRequestDTO show)
    {
        Show = show;
    }
}

[Serializable]
public class UpdateTicketRequest : Request
{
    public UpdateTicketRequestDTO UpdateTicketRequestDTO { get; }

    [JsonConstructor]
    public UpdateTicketRequest(UpdateTicketRequestDTO updateTicketRequestDTO)
    {
        UpdateTicketRequestDTO = updateTicketRequestDTO;
    }
}