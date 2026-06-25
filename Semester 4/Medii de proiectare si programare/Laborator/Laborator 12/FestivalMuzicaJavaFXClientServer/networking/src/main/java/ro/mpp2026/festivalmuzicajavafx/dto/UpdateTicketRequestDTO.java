package ro.mpp2026.festivalmuzicajavafx.dto;

import java.io.Serializable;

public record UpdateTicketRequestDTO(Long ticketId, Long showId, String client, Integer numberOfSeats) implements Serializable {
}
