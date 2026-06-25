package ro.mpp2026.festivalmuzicajavafx.network;

import ro.mpp2026.festivalmuzicajavafx.dto.UpdateTicketRequestDTO;

public record UpdateTicketRequest(UpdateTicketRequestDTO updateTicketRequestDTO) implements Request {
}
