package sm.control.control;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sm.boundary.entity.dto.AuthResponse;
import sm.boundary.entity.dto.ErrorMessages;
import sm.boundary.entity.dto.LoginRequest;
import sm.boundary.entity.dto.RegisterRequest;
import sm.boundary.entity.exceptions.DuplicateEmailException;
import sm.control.boundary.UsersRepository;
import sm.entity.User;
import sm.entity.UserRole;

import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;

    @ConfigProperty(name = "smallrye.jwt.new-token.lifespan", defaultValue = "3600")
    long tokenLifespan;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "jwt.sign.secret")
    String jwtSecret;

    @Transactional
    public void register(RegisterRequest request) {
        usersRepository.find("email", request.email())
                .firstResultOptional()
                .ifPresent(u -> { throw new DuplicateEmailException(ErrorMessages.EMAIL_ALREADY_IN_USE); });

        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(hashPassword(request.password()));
        user.setRole(UserRole.USER);

        usersRepository.persist(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = usersRepository.find("email", request.email())
                .firstResultOptional()
                .orElseThrow(() -> new NotFoundException(ErrorMessages.INVALID_CREDENTIALS));

        if (!verifyPassword(request.password(), user.getPassword())) {
            throw new NotFoundException(ErrorMessages.INVALID_CREDENTIALS);
        }

        return new AuthResponse(generateToken(user));
    }

    private String generateToken(User user) {
        return Jwt.issuer(issuer)
                .subject(user.getId().toString())
                .groups(Set.of(user.getRole().name()))
                .claim("firstName", user.getFirstName())
                .claim("lastName", user.getLastName())
                .claim("email", user.getEmail())
                .expiresIn(tokenLifespan)
                .signWithSecret(jwtSecret);
    }

    private String hashPassword(String plaintext) {
        return BCrypt.withDefaults().hashToString(12, plaintext.toCharArray());
    }

    private boolean verifyPassword(String plaintext, String hashed) {
        return BCrypt.verifyer().verify(plaintext.toCharArray(), hashed).verified;
    }
}
