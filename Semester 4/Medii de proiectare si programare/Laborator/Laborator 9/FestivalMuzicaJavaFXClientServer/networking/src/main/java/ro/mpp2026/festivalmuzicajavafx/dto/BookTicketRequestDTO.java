package ro.mpp2026.festivalmuzicajavafx.dto;

import java.io.Serializable;

public record BookTicketRequestDTO(Long showId, String client, Integer numberOfSeats) implements Serializable {
}
