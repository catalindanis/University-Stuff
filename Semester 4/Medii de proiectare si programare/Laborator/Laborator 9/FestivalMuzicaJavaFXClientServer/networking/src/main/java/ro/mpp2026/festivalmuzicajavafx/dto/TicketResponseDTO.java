package ro.mpp2026.festivalmuzicajavafx.dto;

import java.io.Serializable;

public record TicketResponseDTO(Long id, String clientName, ShowResponseDTO show, int noSeats) implements Serializable {
}
