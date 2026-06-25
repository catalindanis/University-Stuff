package ro.mpp2026.festivalmuzicajavafx.network;

import ro.mpp2026.festivalmuzicajavafx.dto.UserRequestDTO;

public record LoginRequest(UserRequestDTO user) implements Request { }
