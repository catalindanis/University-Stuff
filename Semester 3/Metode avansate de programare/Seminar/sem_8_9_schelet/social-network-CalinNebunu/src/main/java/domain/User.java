package domain;

public abstract class User extends Entity<Long> {
    private String username;
    private String email;
    private String password;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";

    // Constructor
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // --- Getters/Setters ---

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Metoda chemata de 'Event' cand utilizatorul este notificat.
     * (Aceasta este metoda 'update' din Observer Pattern)
     */
    public void receiveNotification(String message) {
        // Implementare simpla: doar afiseaza la consola
        System.out.println(ANSI_CYAN + "[NOTIFICATION for " + this.username + "]: " + message + ANSI_RESET);
    }

    @Override
    public String toString() {
        return String.format("User {ID=%s, Username='%s', Email='%s', Password='%s'}",
                id,
                username,
                email,
                password
        );
    }

}
