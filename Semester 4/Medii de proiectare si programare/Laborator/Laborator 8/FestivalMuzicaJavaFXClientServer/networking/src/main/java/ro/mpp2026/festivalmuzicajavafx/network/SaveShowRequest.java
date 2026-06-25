package ro.mpp2026.festivalmuzicajavafx.network;

import ro.mpp2026.festivalmuzicajavafx.dto.ShowRequestDTO;

public record SaveShowRequest(ShowRequestDTO show) implements Request {
}
