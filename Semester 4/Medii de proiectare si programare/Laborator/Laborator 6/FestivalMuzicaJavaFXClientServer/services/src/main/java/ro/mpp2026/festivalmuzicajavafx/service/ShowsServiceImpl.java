package ro.mpp2026.festivalmuzicajavafx.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;
import ro.mpp2026.festivalmuzicajavafx.repository.ShowsRepository;
import ro.mpp2026.festivalmuzicajavafx.repository.TicketsRepository;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class ShowsServiceImpl implements ShowsService {
    private final ShowsRepository showsRepository;
    private final TicketsRepository ticketsRepository;
    private static final Logger logger = LogManager.getLogger(ShowsServiceImpl.class);
    private final AuthServiceImpl authService;
    private List<Observer> subscribers;

    public ShowsServiceImpl(AuthServiceImpl authService, ShowsRepository showsRepository, TicketsRepository ticketsRepository) {
        this.authService = authService;
        this.showsRepository = showsRepository;
        this.ticketsRepository = ticketsRepository;

        subscribers = new ArrayList<>();
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
        subscribers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer) { subscribers.remove(observer); }

    @Override
    public void notifyObservers() {
        logger.info("Notifying observers");
        for(var observer : subscribers) {
            logger.info("Update {}", observer);
            observer.update();
        }
    }
}
