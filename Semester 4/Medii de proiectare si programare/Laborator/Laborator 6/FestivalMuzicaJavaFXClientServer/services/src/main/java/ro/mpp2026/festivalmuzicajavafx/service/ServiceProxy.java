package ro.mpp2026.festivalmuzicajavafx.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;
import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.dto.*;
import ro.mpp2026.festivalmuzicajavafx.network.*;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceProxy implements Service {
    private final int port;
    private final String host;

    private Observer client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private static final Logger logger = LogManager.getLogger(AuthServiceProxy.class);

    private final BlockingQueue<Response> responses;
    private volatile boolean finished;

    public ServiceProxy(int port, String host) {
        this.port = port;
        this.host = host;

        responses = new LinkedBlockingQueue<>();
    }

    @Override
    public User login(String email, String password, Observer client) {
        initializeConnection();
        UserRequestDTO userRequestDTO = new UserRequestDTO(email, password);
        sendRequest(new LoginRequest(userRequestDTO));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            Map<String, Object> data = okResponse.getData();
            UserResponseDTO userResponseDTO = (UserResponseDTO) data.get("user");
            this.client = client;
            return new User(
                    userResponseDTO.id(),
                    "",
                    ""
            );
        }
        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }

        return null;
    }

    @Override
    public void register(String email, String password) {
        initializeConnection();
        UserRequestDTO userRequestDTO = new UserRequestDTO(email, password);
        sendRequest(new RegisterRequest(userRequestDTO));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            return;
        }
        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }
    }

    @Override
    public void logout(Long userId, Observer client) {
        initializeConnection();
        sendRequest(new LogoutRequest(userId, client));
//        Response response = readResponse();
//
//        if (response instanceof ErrorResponse err){
//            closeConnection();
//            throw new RuntimeException(err.message());
//        }
//
//        closeConnection();
    }

    @Override
    public void save(String artistName, LocalDate date, String location, int remainingSeats) {
        initializeConnection();
        ShowRequestDTO showRequestDTO = new ShowRequestDTO(artistName, date, location, remainingSeats);

        sendRequest( new SaveShowRequest(showRequestDTO));
        Response response = readResponse();
        if (response instanceof OkResponse){
            return;
        }

        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }
    }

    @Override
    public List<Show> findAll() {
        initializeConnection();

        sendRequest(new FindAllShowsRequest(null));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            Map<String, Object> data = okResponse.getData();
            List<ShowResponseDTO> showsResponseDTO = (List<ShowResponseDTO>) data.get("shows");
            return showsResponseDTO.stream().map(dto -> new Show(dto.id(),
                    dto.artistName(),
                    dto.date(),
                    dto.location(),
                    dto.remainingSeats())).toList();
        }

        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }

        return List.of();
    }

    @Override
    public List<Show> findAll(ShowFilter showFilter) {
        initializeConnection();

        sendRequest(new FindAllShowsRequest(showFilter));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            Map<String, Object> data = okResponse.getData();
            List<ShowResponseDTO> showsResponseDTO = (List<ShowResponseDTO>) data.get("shows");
            return showsResponseDTO.stream().map(dto -> new Show(dto.id(),
                    dto.artistName(),
                    dto.date(),
                    dto.location(),
                    dto.remainingSeats())).toList();
        }

        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }

        return List.of();
    }

    @Override
    public Optional<Show> findById(Long id) {
        initializeConnection();

        sendRequest(new FindShowRequest(id));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            Map<String, Object> data = okResponse.getData();
            ShowResponseDTO showsResponseDTO = (ShowResponseDTO) data.get("show");

            if(showsResponseDTO == null)
                return Optional.empty();

            return Optional.of(new Show(showsResponseDTO.id(),
                    showsResponseDTO.artistName(),
                    showsResponseDTO.date(),
                    showsResponseDTO.location(),
                    showsResponseDTO.remainingSeats()));
        }

        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }

        return Optional.empty();
    }

    @Override
    public void bookTicketForShow(Long showId, String client, Integer numberOfSeats) {
        initializeConnection();

        BookTicketRequestDTO bookTicketDTO = new BookTicketRequestDTO(showId, client, numberOfSeats);
        sendRequest(new BookTicketRequest(bookTicketDTO));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            return;
        }

        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }
    }

    @Override
    public void updateTicket(Long ticketId, String client, Long showId, Integer numberOfSeats) {
        initializeConnection();

        UpdateTicketRequestDTO updateTicketRequestDTO = new UpdateTicketRequestDTO(ticketId, showId, client, numberOfSeats);
        sendRequest(new UpdateTicketRequest(updateTicketRequestDTO));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            return;
        }

        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }
    }

    @Override
    public List<Ticket> findAllTickets() {
        initializeConnection();

        sendRequest(new FindAllTicketsRequest());
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            Map<String, Object> data = okResponse.getData();
            List<TicketResponseDTO> ticketsResponseDTO = (List<TicketResponseDTO>) data.get("tickets");
            return ticketsResponseDTO.stream().map(dto -> new Ticket(dto.id(),
                    dto.clientName(),
                    new Show(
                            dto.show().id(),
                            dto.show().artistName(),
                            dto.show().date(),
                            dto.show().location(),
                            dto.show().remainingSeats()
                    ),
                    dto.noSeats())).toList();
        }

        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }

        return List.of();
    }

    @Override
    public List<Ticket> findAllTicketsForShow(Long showId) {
        initializeConnection();

        sendRequest(new FindTicketsForShowRequest(showId));
        Response response = readResponse();
        if (response instanceof OkResponse okResponse){
            Map<String, Object> data = okResponse.getData();
            List<TicketResponseDTO> ticketsResponseDTO = (List<TicketResponseDTO>) data.get("tickets");
            return ticketsResponseDTO.stream().map(dto -> new Ticket(dto.id(),
                    dto.clientName(),
                    new Show(
                            dto.show().id(),
                            dto.show().artistName(),
                            dto.show().date(),
                            dto.show().location(),
                            dto.show().remainingSeats()
                    ),
                    dto.noSeats())).toList();
        }

        if (response instanceof ErrorResponse err){
            closeConnection();
            throw new RuntimeException(err.message());
        }

        return List.of();
    }

    @Override
    public int getNumberOfSoldSeatsForShow(Long showId) {
        return findAllTicketsForShow(showId).stream()
                .mapToInt(Ticket::getNoSeats).sum();
    }

    public void subscribe(Observer observer)
    {
//        observers.Add(observer);
    }

    public void unsubscribe(Observer observer)
    {
//        observers.Remove(observer);
    }

    public void notifyObservers()
    {
        this.client.update();
        //foreach (var observer in observers)
        //    observer.Update();
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            this.client = null;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    private void sendRequest(Request request) {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error sending object " + e);
        }
    }

    private Response readResponse() {
        Response response = null;

        try{
            response = responses.take();
        } catch (InterruptedException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }

        return response;
    }
    private void initializeConnection() {
        if(connection != null && !connection.isClosed())
            return;

        try {
            connection=new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }
    private void startReader(){
        Thread tw = new Thread(new ServiceProxy.ReaderThread());
        tw.start();
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response = input.readObject();
                    logger.debug("response received {}",response);
                    try {
                        if(response instanceof UpdateResponse updateResponse)
                            handleUpdate(updateResponse);
                        else
                            responses.put((Response) response);
                    } catch (InterruptedException e) {
                        logger.error(e);
                        logger.error(e.getStackTrace());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    logger.error("Reading error {}", e);
                }
            }
        }
    }

    private void handleUpdate(UpdateResponse response)
    {
        notifyObservers();
    }
}
