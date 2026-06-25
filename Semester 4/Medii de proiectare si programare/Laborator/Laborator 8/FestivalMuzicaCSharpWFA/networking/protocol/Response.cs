using System.Text.Json.Serialization;
using networking.dto;

namespace networking.protocol;

[JsonPolymorphic(TypeDiscriminatorPropertyName = "$type")]
[JsonDerivedType(typeof(OkResponse), "OkResponse")]
[JsonDerivedType(typeof(ErrorResponse), "ErrorResponse")]
[JsonDerivedType(typeof(UpdateResponse), "UpdateResponse")]
[JsonDerivedType(typeof(UserResponseDTO), "UserResponseDTO")]
[JsonDerivedType(typeof(ShowResponseDTO), "ShowResponseDTO")]
[JsonDerivedType(typeof(TicketResponseDTO), "TicketResponseDTO")]
public class Response 
{
}

[Serializable]
public class OkResponse : Response
{
    public Dictionary<string, object> Data { get; }

    public OkResponse()
    {
        Data = new Dictionary<string, object>();
    }

    [JsonConstructor]
    public OkResponse(Dictionary<string, object> data)
    {
        Data = data ?? new Dictionary<string, object>(); 
    }

    public void AddData(string key, object value)
    {
        Data[key] = value;
    }
}

[Serializable]
public class ErrorResponse : Response
{
    public string Message { get; }

    [JsonConstructor]
    public ErrorResponse(string message)
    {
        Message = message;
    }
}

[Serializable]
public class UpdateResponse : Response
{
}