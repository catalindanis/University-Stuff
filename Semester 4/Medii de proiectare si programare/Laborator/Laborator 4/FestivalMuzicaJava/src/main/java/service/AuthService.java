package service;

import domain.User;
import lombok.Getter;
import repository.UsersDBRepository;
import repository.UsersRepository;
import utils.JdbcUtils;

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
            props.load(AuthService.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            System.out.println("Cannot find db.properties " + e);
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
            System.out.println(exception);
            throw new RuntimeException("Password encryption failed!");
        }
    }
}
