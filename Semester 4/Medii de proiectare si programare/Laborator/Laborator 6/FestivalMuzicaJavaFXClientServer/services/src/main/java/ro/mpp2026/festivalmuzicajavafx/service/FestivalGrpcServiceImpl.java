package ro.mpp2026.festivalmuzicajavafx.service;

import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;
import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.grpc.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FestivalGrpcServiceImpl extends FestivalServiceGrpc.FestivalServiceImplBase {

    private static final Logger logger = LogManager.getLogger(FestivalGrpcServiceImpl.class);

    private final ServiceImpl service;

    private final Map<Long, StreamObserver<ServerUpdate>> subscribedClients = new ConcurrentHashMap<>();

    public FestivalGrpcServiceImpl(ServiceImpl service) {
        this.service = service;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            ro.mpp2026.festivalmuzicajavafx.utils.Observer grpcObserver = () -> {
                notifyAllSubscribers();
            };

            User user = service.login(
                    request.getUser().getEmail(),
                    request.getUser().getPassword(),
                    grpcObserver
            );

            UserResponse response = UserResponse.newBuilder()
                    .setId(user.getId())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("Login error: {}", e.getMessage());
            responseObserver.onError(
                    Status.UNAUTHENTICATED.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<OkResponse> responseObserver) {
        try {
            service.register(
                    request.getUser().getEmail(),
                    request.getUser().getPassword()
            );

            responseObserver.onNext(OkResponse.newBuilder().build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("Register error: {}", e.getMessage());
            responseObserver.onError(
                    Status.ALREADY_EXISTS.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void logout(LogoutRequest request, StreamObserver<OkResponse> responseObserver) {
        try {
            service.logout(request.getUserId(), null);

            StreamObserver<ServerUpdate> sub = subscribedClients.remove(request.getUserId());
            if (sub != null) {
                sub.onCompleted();
            }

            responseObserver.onNext(OkResponse.newBuilder().build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("Logout error: {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void subscribe(UserResponse request, StreamObserver<ServerUpdate> responseObserver) {
        subscribedClients.put(request.getId(), responseObserver);
        logger.info("Client {} subscribed for updates", request.getId());
    }

    private void notifyAllSubscribers() {
        ServerUpdate update = ServerUpdate.newBuilder()
                .setType("DATA_CHANGED")
                .build();

        for (Map.Entry<Long, StreamObserver<ServerUpdate>> entry : subscribedClients.entrySet()) {
            try {
                entry.getValue().onNext(update);
            } catch (Exception e) {
                logger.error("Error notifying subscriber {}: {}", entry.getKey(), e.getMessage());
                subscribedClients.remove(entry.getKey());
            }
        }
    }

    @Override
    public void saveShow(ShowRequest request, StreamObserver<OkResponse> responseObserver) {
        try {
            LocalDate date = timestampToLocalDate(request.getDate());

            service.save(
                    request.getArtistName(),
                    date,
                    request.getLocation(),
                    request.getNumberOfSeats()
            );

            responseObserver.onNext(OkResponse.newBuilder().build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("SaveShow error: {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void findAllShows(FindAllShowsRequest request, StreamObserver<ShowListResponse> responseObserver) {
        try {
            List<Show> shows;

            if (request.hasFilter()) {
                ro.mpp2026.festivalmuzicajavafx.grpc.ShowFilter protoFilter = request.getFilter();

                String artistName = protoFilter.getArtistName().isEmpty() ? null : protoFilter.getArtistName();
                LocalDate date = protoFilter.hasDate() && protoFilter.getDate().getSeconds() != 0
                        ? timestampToLocalDate(protoFilter.getDate())
                        : null;

                ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter domainFilter =
                        ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter.builder()
                                .artistName(artistName)
                                .date(date)
                                .build();
                shows = service.findAll(domainFilter);
            } else {
                shows = service.findAll();
            }

            ShowListResponse response = ShowListResponse.newBuilder()
                    .addAllShows(shows.stream().map(this::showToProto).toList())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("FindAllShows error: {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }
    @Override
    public void findShowById(FindShowRequest request, StreamObserver<ShowResponse> responseObserver) {
        try {
            Optional<Show> show = service.findById(request.getId());

            if (show.isEmpty()) {
                responseObserver.onError(
                        Status.NOT_FOUND.withDescription("Show not found").asRuntimeException()
                );
                return;
            }

            responseObserver.onNext(showToProto(show.get()));
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("FindShowById error: {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void bookTicket(BookTicketRequest request, StreamObserver<OkResponse> responseObserver) {
        try {
            service.bookTicketForShow(
                    request.getShowId(),
                    request.getClient(),
                    request.getNumberOfSeats()
            );

            responseObserver.onNext(OkResponse.newBuilder().build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("BookTicket error: {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void updateTicket(UpdateTicketRequest request, StreamObserver<OkResponse> responseObserver) {
        try {
            service.updateTicket(
                    request.getTicketId(),
                    request.getClient(),
                    request.getShowId(),
                    request.getNumberOfSeats()
            );

            responseObserver.onNext(OkResponse.newBuilder().build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("UpdateTicket error: {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void findAllTickets(OkResponse request, StreamObserver<TicketListResponse> responseObserver) {
        try {
            List<Ticket> tickets = service.findAllTickets();

            TicketListResponse response = TicketListResponse.newBuilder()
                    .addAllTickets(tickets.stream().map(this::ticketToProto).toList())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("FindAllTickets error: {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void findTicketsForShow(FindTicketsForShowRequest request, StreamObserver<TicketListResponse> responseObserver) {
        try {
            List<Ticket> tickets = service.findAllTicketsForShow(request.getShowId());

            TicketListResponse response = TicketListResponse.newBuilder()
                    .addAllTickets(tickets.stream().map(this::ticketToProto).toList())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("FindTicketsForShow error: {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    @Override
    public void getSoldSeatsForShow(FindTicketsForShowRequest request, StreamObserver<SoldSeatsResponse> responseObserver) {
        try {
            int soldSeats = service.getNumberOfSoldSeatsForShow(request.getShowId());

            responseObserver.onNext(SoldSeatsResponse.newBuilder().setSoldSeats(soldSeats).build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            logger.error("GetSoldSeatsForShow error: {}", e.getMessage());
            responseObserver.onError(
                    Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException()
            );
        }
    }

    private ShowResponse showToProto(Show show) {
        return ShowResponse.newBuilder()
                .setId(show.getId())
                .setArtistName(show.getArtistName())
                .setDate(localDateToTimestamp(show.getDate()))
                .setLocation(show.getLocation())
                .setRemainingSeats(show.getRemainingSeats())
                .build();
    }

    private TicketResponse ticketToProto(Ticket ticket) {
        return TicketResponse.newBuilder()
                .setId(ticket.getId())
                .setClientName(ticket.getClientName())
                .setShow(showToProto(ticket.getShow()))
                .setNoSeats(ticket.getNoSeats())
                .build();
    }

    private Timestamp localDateToTimestamp(LocalDate date) {
        return Timestamp.newBuilder()
                .setSeconds(date.atStartOfDay().toEpochSecond(ZoneOffset.UTC))
                .build();
    }

    private LocalDate timestampToLocalDate(Timestamp timestamp) {
        return java.time.Instant.ofEpochSecond(timestamp.getSeconds())
                .atZone(ZoneOffset.UTC)
                .toLocalDate();
    }
}
