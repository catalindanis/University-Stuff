package ro.mpp2026.festivalmuzicajavafx.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;
import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.repository.ShowsRepository;
import ro.mpp2026.festivalmuzicajavafx.repository.TicketsRepository;
import ro.mpp2026.festivalmuzicajavafx.repository.UsersRepository;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.StreamSupport;

public class ServiceImpl implements Service {
    private final UsersRepository repository;
    private final String encryptionAESKey;
    private final Map<String, Observer> loggedClients;
    private final ShowsRepository showsRepository;
    private final TicketsRepository ticketsRepository;
    private static final Logger logger = LogManager.getLogger(ServiceImpl.class);

    public ServiceImpl(UsersRepository usersRepository, ShowsRepository showsRepository, TicketsRepository ticketsRepository, String encryptionAESKey) {
        this.repository = usersRepository;
        this.encryptionAESKey = encryptionAESKey;
        this.loggedClients = new HashMap<>();
        this.showsRepository = showsRepository;
        this.ticketsRepository = ticketsRepository;
    }

    public User login(String email, String password, Observer client) {
        String encryptedPassword = encrypt(password, encryptionAESKey);

        Optional<User> user = repository.findByEmailAndPassword(email, encryptedPassword);

        if(user.isEmpty())
            throw new RuntimeException("Invalid credentials");

        if(loggedClients.containsKey(user.get().getId().toString()))
            throw new RuntimeException("User already logged in");

        loggedClients.put(user.get().getId().toString(), client);
        return user.get();
    }

    public void register(String email, String password) {
        String encryptedPassword = encrypt(password, encryptionAESKey);

        Optional<User> user = repository.findByEmailAndPassword(email, encryptedPassword);

        if(user.isPresent())
            throw new RuntimeException("Email already exists");

        repository.save(new User(0L, email, encryptedPassword));
    }

    @Override
    public void logout(Long userId, Observer client) {
        loggedClients.remove(userId.toString());
    }

    private String encrypt(String data, String key) {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (Exception exception) {
            throw new RuntimeException("Password encryption failed!");
        }
    }

    public void save(String artistName, LocalDate date, String location, int remainingSeats) {
        Show show = new Show(0L, artistName, date, location, remainingSeats);
        showsRepository.save(show);
        notifyObservers();
    }

    public List<Show> findAll() {
        return StreamSupport.stream(showsRepository.findAll().spliterator(), false)
                .toList();
    }

    public List<Show> findAll(ShowFilter showFilter) {
        return StreamSupport.stream(showsRepository.findAll(showFilter).spliterator(), false)
                .toList();
    }

    public Optional<Show> findById(Long id) {
        Show show = showsRepository.findOne(id);
        return Optional.ofNullable(show);
    }

    public void bookTicketForShow(Long showId, String client, Integer numberOfSeats) {
        Show show = showsRepository.findOne(showId);

        if(show.getRemainingSeats() < numberOfSeats)
            throw new RuntimeException("Not enough seats");

        Ticket ticket = new Ticket(0L, client, show, numberOfSeats);
        show.setRemainingSeats(show.getRemainingSeats() - numberOfSeats);

        ticketsRepository.save(ticket);
        showsRepository.update(showId, show);

        notifyObservers();
    }

    public void updateTicket(Long ticketId, String client, Long showId, Integer numberOfSeats) {
        Ticket ticket = ticketsRepository.findOne(ticketId);
        Show show = ticket.getShow();

        if(show.getRemainingSeats() + ticket.getNoSeats() - numberOfSeats < 0)
            throw new RuntimeException("Not enough seats");

        show.setRemainingSeats(show.getRemainingSeats() + ticket.getNoSeats() - numberOfSeats);
        ticket.setNoSeats(numberOfSeats);

        ticketsRepository.update(ticketId, ticket);
        showsRepository.update(show.getId(), show);

        notifyObservers();
    }

    public List<Ticket> findAllTickets() {
        return StreamSupport.stream(ticketsRepository.findAll().spliterator(), false)
                .toList();
    }

    public List<Ticket> findAllTicketsForShow(Long showId) {
        return StreamSupport.stream(ticketsRepository.findAllForShow(showId).spliterator(), false)
                .toList();
    }

    public int getNumberOfSoldSeatsForShow(Long showId) {
        return findAllTicketsForShow(showId).stream()
                .mapToInt(Ticket::getNoSeats)
                .sum();
    }

    @Override
    public void subscribe(Observer observer) {
    }

    @Override
    public void unsubscribe(Observer observer) {  }

    @Override
    public void notifyObservers() {
        for(var observer : loggedClients.values())
            observer.update();
    }
}
