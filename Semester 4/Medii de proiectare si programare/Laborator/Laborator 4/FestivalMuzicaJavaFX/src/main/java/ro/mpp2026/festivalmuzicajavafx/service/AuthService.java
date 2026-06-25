package ro.mpp2026.festivalmuzicajavafx.service;

import lombok.Getter;
import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.repository.UsersDBRepository;
import ro.mpp2026.festivalmuzicajavafx.repository.UsersRepository;
import ro.mpp2026.festivalmuzicajavafx.utils.JdbcUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

public class AuthService {
    @Getter
    private static final AuthService instance = new AuthService();
    private final UsersRepository repository;
    private final String encryptionAESKey;

    private AuthService() {
        Properties props = new Properties();
        try {
            var inStream = AuthService.class.getResourceAsStream("/ro/mpp2026/festivalmuzicajavafx/db.properties");
            if (inStream == null) {
                throw new RuntimeException("Cannot find db.properties in classpath at /ro/mpp2026/festivalmuzicajavafx/db.properties");
            }
            props.load(inStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading db.properties", e);
        }

        JdbcUtils jdbcUtils = new JdbcUtils(props);
        repository = new UsersDBRepository(jdbcUtils);

        encryptionAESKey = props.getProperty("jdbc.eckey");
    }

    public User login(String email, String password) {
        String encryptedPassword = encrypt(password, encryptionAESKey);

        Optional<User> user = repository.findByEmailAndPassword(email, encryptedPassword);

        if(user.isEmpty())
            throw new RuntimeException("Invalid credentials");

        return user.get();
    }

    public void register(String email, String password) {
        String encryptedPassword = encrypt(password, encryptionAESKey);

        Optional<User> user = repository.findByEmailAndPassword(email, encryptedPassword);

        if(user.isPresent())
            throw new RuntimeException("Email already exists");

        repository.save(new User(0L, email, encryptedPassword));
    }

    public String encrypt(String data, String key) {
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
}
