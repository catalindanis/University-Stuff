using FestivalMuzicaCSharp.Domain;
using Google.Protobuf.WellKnownTypes;
using Grpc.Net.Client;
using log4net;
using services.Utils;

namespace networking.protocol;

public class ServerProxy : IService
{
    private readonly string host;
    private readonly int port;

    private IObserver? client;
    private Grpc.Net.Client.GrpcChannel? channel;
    private FestivalGrpc.FestivalService.FestivalServiceClient? grpcClient;

    private CancellationTokenSource? subscriptionCts;

    private readonly ILog Log = LogManager.GetLogger(typeof(ServerProxy));

    public ServerProxy(string host, int port)
    {
        this.host = host;
        this.port = port;
    }

    private void InitializeConnection()
    {
        if (grpcClient != null)
            return;

        AppContext.SetSwitch("System.Net.Http.SocketsHttpHandler.Http2UnencryptedSupport", true);

        channel = Grpc.Net.Client.GrpcChannel.ForAddress($"http://{host}:{port}");
        grpcClient = new FestivalGrpc.FestivalService.FestivalServiceClient(channel);
    }

    private void CloseConnection()
    {
        subscriptionCts?.Cancel();
        channel?.Dispose();
        grpcClient = null;
        client = null;
    }

    public User Login(string email, string password, IObserver client)
    {
        InitializeConnection();

        try
        {
            var response = grpcClient!.Login(new FestivalGrpc.LoginRequest
            {
                User = new FestivalGrpc.UserRequest { Email = email, Password = password }
            });

            this.client = client;
            StartSubscription(response.Id);

            return new User(response.Id, "", "");
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public void Register(string email, string password)
    {
        InitializeConnection();

        try
        {
            grpcClient!.Register(new FestivalGrpc.RegisterRequest
            {
                User = new FestivalGrpc.UserRequest { Email = email, Password = password }
            });
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public void Logout(long userId, IObserver client)
    {
        try
        {
            grpcClient!.Logout(new FestivalGrpc.LogoutRequest { UserId = userId });
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
        finally
        {
            CloseConnection();
        }
    }

    private void StartSubscription(long userId)
    {
        subscriptionCts = new CancellationTokenSource();

        Task.Run(async () =>
        {
            try
            {
                using var streamingCall = grpcClient!.Subscribe(
                    new FestivalGrpc.UserResponse { Id = userId },
                    cancellationToken: subscriptionCts.Token
                );

                while (await streamingCall.ResponseStream.MoveNext(subscriptionCts.Token))
                {
                    var update = streamingCall.ResponseStream.Current;
                    Log.Info($"Update primit de la server: {update.Type}");
                    NotifyObservers();
                }
            }
            catch (Grpc.Core.RpcException ex) when (ex.StatusCode == Grpc.Core.StatusCode.Cancelled)
            {
                Log.Info("Subscription oprita (logout).");
            }
            catch (Exception ex)
            {
                Log.Error($"Eroare subscription: {ex.Message}");
            }
        });
    }

    public void Save(string artistName, DateOnly date, string location, int noSeats)
    {
        try
        {
            grpcClient!.SaveShow(new FestivalGrpc.ShowRequest
            {
                ArtistName = artistName,
                Date = DateOnlyToTimestamp(date),
                Location = location,
                NumberOfSeats = noSeats
            });
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public List<Show> FindAll()
    {
        try
        {
            var response = grpcClient!.FindAllShows(new FestivalGrpc.FindAllShowsRequest());
            return response.Shows.Select(ProtoToShow).ToList();
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public List<Show> FindAll(ShowFilter showFilter)
    {
        try
        {
            var request = new FestivalGrpc.FindAllShowsRequest();

            if (!string.IsNullOrEmpty(showFilter.ArtistName) || showFilter.Date.HasValue)
            {
                request.Filter = new FestivalGrpc.ShowFilter
                {
                    ArtistName = showFilter.ArtistName ?? "",
                    Date = showFilter.Date.HasValue
                        ? DateOnlyToTimestamp(showFilter.Date.Value)
                        : new Timestamp()
                };
            }

            var response = grpcClient!.FindAllShows(request);
            return response.Shows.Select(ProtoToShow).ToList();
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public Show? FindOne(long id)
    {
        try
        {
            var response = grpcClient!.FindShowById(new FestivalGrpc.FindShowRequest { Id = id });
            return ProtoToShow(response);
        }
        catch (Grpc.Core.RpcException ex) when (ex.StatusCode == Grpc.Core.StatusCode.NotFound)
        {
            return null;
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public void BookTicketForShow(long showId, string clientName, int noSeats)
    {
        try
        {
            grpcClient!.BookTicket(new FestivalGrpc.BookTicketRequest
            {
                ShowId = showId,
                Client = clientName,
                NumberOfSeats = noSeats
            });
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public void UpdateTicket(long ticketId, long showId, string clientName, int noSeats)
    {
        try
        {
            grpcClient!.UpdateTicket(new FestivalGrpc.UpdateTicketRequest
            {
                TicketId = ticketId,
                ShowId = showId,
                Client = clientName,
                NumberOfSeats = noSeats
            });
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public List<Ticket> FindAllTickets()
    {
        try
        {
            var response = grpcClient!.FindAllTickets(new FestivalGrpc.OkResponse());
            return response.Tickets.Select(ProtoToTicket).ToList();
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public List<Ticket> FindAllTicketsForShow(long showId)
    {
        try
        {
            var response = grpcClient!.FindTicketsForShow(
                new FestivalGrpc.FindTicketsForShowRequest { ShowId = showId });
            return response.Tickets.Select(ProtoToTicket).ToList();
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }

    public int GetNumberOfSoldSeatsForShow(long showId)
    {
        try
        {
            var response = grpcClient!.GetSoldSeatsForShow(
                new FestivalGrpc.FindTicketsForShowRequest { ShowId = showId });
            return response.SoldSeats;
        }
        catch (Grpc.Core.RpcException ex)
        {
            throw new Exception(ex.Status.Detail);
        }
    }
    public void Subscribe(IObserver observer) { }

    public void Unsubscribe(IObserver observer) { }

    public void NotifyObservers()
    {
        this.client?.Update();
    }

    private Show ProtoToShow(FestivalGrpc.ShowResponse proto)
    {
        return new Show(
            proto.Id,
            proto.ArtistName,
            DateOnly.FromDateTime(proto.Date.ToDateTime()),
            proto.Location,
            proto.RemainingSeats
        );
    }

    private Ticket ProtoToTicket(FestivalGrpc.TicketResponse proto)
    {
        return new Ticket(
            proto.Id,
            proto.ClientName,
            ProtoToShow(proto.Show),
            proto.NoSeats
        );
    }

    private Timestamp DateOnlyToTimestamp(DateOnly date)
    {
        return Timestamp.FromDateTime(
            DateTime.SpecifyKind(date.ToDateTime(TimeOnly.MinValue), DateTimeKind.Utc)
        );
    }
}
