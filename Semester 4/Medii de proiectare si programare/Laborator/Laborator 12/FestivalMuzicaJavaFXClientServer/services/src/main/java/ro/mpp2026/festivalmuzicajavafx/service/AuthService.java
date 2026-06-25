package ro.mpp2026.festivalmuzicajavafx.service;

import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

public interface AuthService {
    User login(String email, String password, Observer client);
    void register(String email, String password);
    void logout(Long userId, Observer client);
}
