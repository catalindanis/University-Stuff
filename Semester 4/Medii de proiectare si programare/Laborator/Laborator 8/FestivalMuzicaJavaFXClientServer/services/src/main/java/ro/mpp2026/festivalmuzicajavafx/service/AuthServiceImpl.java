package ro.mpp2026.festivalmuzicajavafx.service;

import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.repository.UsersRepository;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private final UsersRepository repository;
    private final String encryptionAESKey;
    private final List<Long> loggedUsers;

    public AuthServiceImpl(UsersRepository repository, String encryptionAESKey) {
        this.repository = repository;
        this.encryptionAESKey = encryptionAESKey;
        this.loggedUsers = new ArrayList<>();
    }

    public User login(String email, String password, Observer client) {
        String encryptedPassword = encrypt(password, encryptionAESKey);

        Optional<User> user = repository.findByEmailAndPassword(email, encryptedPassword);

        if(user.isEmpty())
            throw new RuntimeException("Invalid credentials");

        if(loggedUsers.contains(user.get().getId()))
            throw new RuntimeException("User already logged in");

        loggedUsers.add(user.get().getId());
        return user.get();
    }

    public void register(String email, String password) {
        String encryptedPassword = encrypt(password, encryptionAESKey);

        Optional<User> user = repository.findByEmailAndPassword(email, encryptedPassword);

        if(user.isPresent())
            throw new RuntimeException("Email already exists");

        repository.save(new User(0L, email, encryptedPassword));
    }

    @Override
    public void logout(Long userId, Observer client) {
        loggedUsers.remove(userId);
    }

    private String encrypt(String data, String key) {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        }
        catch (Exception exception) {
            throw new RuntimeException("Password encryption failed!");
        }
    }

    public boolean isLoggedIn(Long userId) {
        return loggedUsers.contains(userId);
    }
}
