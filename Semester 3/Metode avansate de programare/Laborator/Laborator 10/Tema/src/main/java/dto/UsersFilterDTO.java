package dto;

import lombok.Getter;
import lombok.Setter;
import models.DuckType;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
public class UsersFilterDTO {
    public Optional<String> username;
    public Optional<String> email;
    public Optional<String> password;
    public Optional<String> firstName;
    public Optional<String> lastName;
    public Optional<LocalDate> dateOfBirth;
    public Optional<String> occupation;
    public Optional<Integer> empathyLevel;
    public Optional<DuckType> type;
    public Optional<Double> speed;
    public Optional<Double> resistance;
    public Optional<Long> group;
    public Optional<Class> userType;
    public Optional<Boolean> loginAction;

    public UsersFilterDTO() {
        username = Optional.empty();
        email = Optional.empty();
        password = Optional.empty();
        firstName = Optional.empty();
        lastName = Optional.empty();
        dateOfBirth = Optional.empty();
        occupation = Optional.empty();
        empathyLevel = Optional.empty();
        type = Optional.empty();
        speed = Optional.empty();
        resistance = Optional.empty();
        group = Optional.empty();
        userType = Optional.empty();
        loginAction = Optional.empty();
    }
}
