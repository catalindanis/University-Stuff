package ro.mpp2026.festivalmuzicajavafx.service;

import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;
import ro.mpp2026.festivalmuzicajavafx.utils.Observable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShowsService extends Observable {
    void save(String artistName, LocalDate date, String location, int remainingSeats);

    List<Show> findAll();

    List<Show> findAll(ShowFilter showFilter);

    Optional<Show> findById(Long id);

    void bookTicketForShow(Long showId, String client, Integer numberOfSeats);

    void updateTicket(Long ticketId, String client, Long showId, Integer numberOfSeats);

    List<Ticket> findAllTickets();

    List<Ticket> findAllTicketsForShow(Long showId);

    int getNumberOfSoldSeatsForShow(Long showId);
}
