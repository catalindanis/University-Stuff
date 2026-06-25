package ro.mpp2026.festivalmuzicajavafx.service;

import lombok.Getter;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;
import ro.mpp2026.festivalmuzicajavafx.repository.ShowsDBRepository;
import ro.mpp2026.festivalmuzicajavafx.repository.ShowsRepository;
import ro.mpp2026.festivalmuzicajavafx.repository.TicketsDBRepository;
import ro.mpp2026.festivalmuzicajavafx.repository.TicketsRepository;
import ro.mpp2026.festivalmuzicajavafx.utils.JdbcUtils;
import ro.mpp2026.festivalmuzicajavafx.utils.Observable;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.StreamSupport;

public class ShowsService implements Observable {
    @Getter
    private static final ShowsService instance = new ShowsService();

    private final ShowsRepository showsRepository;
    private final TicketsRepository ticketsRepository;

    List<Observer> subscribers;

    private ShowsService() {
        Properties props = new Properties();
        try {
            var inStream = AuthService.class.getResourceAsStream("/ro/mpp2026/festivalmuzicajavafx/db.properties");
            if (inStream == null) {
                throw new RuntimeException("Cannot find db.properties in classpath at /ro/mpp2026/festivalmuzicajavafx/db.properties");
            }
            props.load(inStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading db.properties", e);
        }

        JdbcUtils jdbcUtils = new JdbcUtils(props);
        showsRepository = new ShowsDBRepository(jdbcUtils);
        ticketsRepository = new TicketsDBRepository(jdbcUtils);

        subscribers = new ArrayList<>();
    }

    public void save(String artistName, LocalDate date, String location, int remainingSeats) {
        Show show = new Show(0L, artistName, date, location, remainingSeats);
        showsRepository.save(show);
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
    public void notifyObservers() {
        for(var observer : subscribers)
            observer.update();
    }
}
