package ro.mpp2026.festivalmuzicajavafx.repository;

import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;

public interface TicketsRepository extends Repository<Long, Ticket> {
    Iterable<Ticket> findAllForShow(Long showId);
}
