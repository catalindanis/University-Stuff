package ro.mpp2026.festivalmuzicajavafx.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;
import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.dto.*;
import ro.mpp2026.festivalmuzicajavafx.mappers.Mapper;
import ro.mpp2026.festivalmuzicajavafx.mappers.ShowMapper;
import ro.mpp2026.festivalmuzicajavafx.mappers.TicketMapper;
import ro.mpp2026.festivalmuzicajavafx.mappers.UserMapper;
import ro.mpp2026.festivalmuzicajavafx.network.*;
import ro.mpp2026.festivalmuzicajavafx.service.Service;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class MusicFestivalObjectWorker implements Runnable, Observer {
    private final Socket connection;
    private final Service service;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    private static final Logger logger = LogManager.getLogger(MusicFestivalObjectWorker.class);

    private final Mapper<User, UserResponseDTO> userMapper = new UserMapper();
    private final Mapper<Show, ShowResponseDTO> showMapper = new ShowMapper();
    private final Mapper<Ticket, TicketResponseDTO> ticketMapper = new TicketMapper();

    private User authenticatedUser;

    public MusicFestivalObjectWorker(Socket connection, Service service) {
        this.connection = connection;
        this.service = service;

        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }

        service.subscribe(this);
    }

    @Override
    public void run() {
        while(connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                sendResponse(response);
            } catch (IOException | ClassNotFoundException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
                connected = false;
            }
        }

        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error "+e);
        }
    }

    private Response handleRequest(Request request) {
        if (request instanceof LoginRequest loginRequest) {
            logger.debug("Login request ...");

            UserRequestDTO userRequestDTO = loginRequest.user();

            try {
                User user = service.login(userRequestDTO.email(), userRequestDTO.password(), this);
                this.authenticatedUser = user;
                OkResponse response = new OkResponse();
                response.addData("user", userMapper.convert(user));
                return response;
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof RegisterRequest registerRequest) {
            logger.debug("Register request ...");

            UserRequestDTO userRequestDTO = registerRequest.user();

            try {
                service.register(userRequestDTO.email(), userRequestDTO.password());
                return new OkResponse();
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof SaveShowRequest saveShowRequest) {
            logger.debug("Save show request ...");

            ShowRequestDTO showRequestDTO = saveShowRequest.show();

            try {
                service.save(showRequestDTO.artistName(), showRequestDTO.date(), showRequestDTO.location(), showRequestDTO.remainingSeats());
                return new OkResponse();
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof FindAllShowsRequest findAllShowsRequest) {
            logger.info("Find all shows request ...");

            ShowFilter showFilter = findAllShowsRequest.showFilter();

            try {
                List<ShowResponseDTO> shows = showMapper.convertToList(service.findAll(showFilter));
                logger.info(shows);
                OkResponse response = new OkResponse();
                response.addData("shows", shows);
                return response;
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof FindShowRequest findShowRequest) {
            logger.debug("Find show request ...");

            Long id = findShowRequest.id();

            try {
                Optional<Show> show = service.findById(id);
                ShowResponseDTO showDTO = null;
                if(show.isPresent())
                    showDTO = showMapper.convert(show.get());
                OkResponse response = new OkResponse();
                response.addData("show", showDTO);
                return response;
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof BookTicketRequest bookTicketRequest) {
            logger.debug("Book ticket request ...");

            BookTicketRequestDTO bookTicketDTO = bookTicketRequest.bookTicketDTO();

            try {
                service.bookTicketForShow(
                        bookTicketDTO.showId(),
                        bookTicketDTO.client(),
                        bookTicketDTO.numberOfSeats());
                return new OkResponse();
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof UpdateTicketRequest updateTicketRequest) {
            logger.debug("Update ticket request ...");

            UpdateTicketRequestDTO updateTicketDTO = updateTicketRequest.updateTicketRequestDTO();

            try {
                service.updateTicket(
                        updateTicketDTO.ticketId(),
                        updateTicketDTO.client(),
                        updateTicketDTO.showId(),
                        updateTicketDTO.numberOfSeats());
                return new OkResponse();
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof FindAllTicketsRequest findAllTicketsRequest) {
            logger.debug("Find all tickets request ...");

            try {
                List<TicketResponseDTO> tickets = ticketMapper.convertToList(service.findAllTickets());
                OkResponse response = new OkResponse();
                response.addData("tickets", tickets);
                return response;
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof FindTicketsForShowRequest findTicketsForShowRequest) {
            logger.debug("Find tickets request ...");

            Long showId = findTicketsForShowRequest.showId();
            try {
                List<TicketResponseDTO> tickets = ticketMapper.convertToList(service.findAllTicketsForShow(showId));
                OkResponse response = new OkResponse();
                response.addData("tickets", tickets);
                return response;
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof LogoutRequest logoutRequest) {
            logger.debug("Logout request ...");

            Long userId = logoutRequest.userId();
            try {
                service.logout(userId, this);
                Thread.currentThread().interrupt();
                return new OkResponse();
            } catch (RuntimeException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }

        return new ErrorResponse("Unknown request");
    }

    @Override
    public void update() {
        logger.debug("Update requested ...");
        try {
            sendResponse(new UpdateResponse());
        } catch (IOException e) {
            connected=false;
        }
    }

    private void sendResponse(Response response) throws IOException {
        logger.debug("sending response {}",response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}
