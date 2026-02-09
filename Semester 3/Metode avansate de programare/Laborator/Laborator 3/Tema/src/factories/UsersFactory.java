package factories;

import exceptions.UserException;
import models.*;
import validators.DuckValidator;
import validators.PersonValidator;
import validators.Validator;

import java.time.LocalDate;

public class UsersFactory implements Factory<User> {
    private static final UsersFactory instance = new UsersFactory();
    private final Validator<? super Person> personValidator;
    private final Validator<? super Duck> duckValidator;

    private UsersFactory() {
        personValidator = new PersonValidator();
        duckValidator = new DuckValidator();
    }

    public static UsersFactory getInstance() {
        return instance;
    }

    public User createPerson(long id,
                             String username,
                             String email,
                             String password,
                             String firstName,
                             String lastName,
                             LocalDate dateOfBirth,
                             String occupation,
                             int empathyLevel) {

        Person person = new Person(id,
                username,
                email,
                password,
                firstName,
                lastName,
                dateOfBirth,
                occupation,
                empathyLevel);

        if(!personValidator.validate(person))
            throw new UserException("Invalid person");

        return person;
    }

    public User createDuck(long id,
                           String username,
                           String email,
                           String password,
                           DuckType type,
                           double speed,
                           double resistance,
                           Group group) {

        Duck duck = switch (type) {
            case FLYING -> new FlyingDuck(id,
                    username,
                    email,
                    password,
                    type,
                    speed,
                    resistance,
                    group);
            case SWIMMING -> new SwimmingDuck(id,
                    username,
                    email,
                    password,
                    type,
                    speed,
                    resistance,
                    group);
            case FLYING_AND_SWIMMING -> new FlyingSwimmingDuck(id,
                    username,
                    email,
                    password,
                    type,
                    speed,
                    resistance,
                    group);
        };

        if(!duckValidator.validate(duck))
            throw new UserException("Invalid duck");

        return duck;
    }
}
