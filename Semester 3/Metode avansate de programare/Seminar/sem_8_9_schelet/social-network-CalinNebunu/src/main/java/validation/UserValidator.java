package validation;

import domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User object) throws ValidationException {
        // --- Validari initiale ---
        if (object == null) {
            throw new ValidationException("User cannot be null");
        }
        if (object.getId() == null) {
            throw new ValidationException("User's id cannot be null");
        }
        if (object.getId() < 0) {
            throw new ValidationException("User's id cannot be negative");
        }

        // --- Validare Username ---
        String username = object.getUsername();
        if (username == null || username.trim().isEmpty()) {
            // Prinde null, "" si "   "
            throw new ValidationException("User's username is required");
        }
        String trimmedUsername = username.trim();
        if (trimmedUsername.length() < 5 || trimmedUsername.length() > 50) {
            throw new ValidationException("User's username length must be between 5 and 50 characters");
        }

        // --- Validare Password ---
        String password = object.getPassword();
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException("User's password is required");
        }
        String trimmedPassword = password.trim();
        if (trimmedPassword.length() < 5 || trimmedPassword.length() > 50) {
            throw new ValidationException("User's password length must be between 5 and 50 characters");
        }

        // --- Validare Email ---
        String email = object.getEmail();
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("User's email is required");
        }
        String trimmedEmail = email.trim();
        if (trimmedEmail.length() < 5 || trimmedEmail.length() > 50) {
            throw new ValidationException("User's email length must be between 5 and 50 characters");
        }
        if (!trimmedEmail.contains("@")) {
            throw new ValidationException("User's email address must contain @");
        }
    }
}