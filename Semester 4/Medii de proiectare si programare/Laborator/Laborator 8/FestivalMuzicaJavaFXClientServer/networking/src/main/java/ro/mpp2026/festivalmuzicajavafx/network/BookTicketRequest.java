package ro.mpp2026.festivalmuzicajavafx.network;

import ro.mpp2026.festivalmuzicajavafx.dto.BookTicketRequestDTO;

public record BookTicketRequest(BookTicketRequestDTO bookTicketDTO) implements Request {
}
