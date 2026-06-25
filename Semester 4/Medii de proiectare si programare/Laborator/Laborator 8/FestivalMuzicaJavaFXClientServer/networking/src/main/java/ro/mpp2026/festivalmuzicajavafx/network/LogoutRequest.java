package ro.mpp2026.festivalmuzicajavafx.network;

import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

public record LogoutRequest(Long userId, Observer client) implements Request {
}
