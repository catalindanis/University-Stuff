package models;

import java.io.Serial;
import java.io.Serializable;

public abstract class User extends Entity<Long> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected String username;
    protected String email;
    protected String password;

    public User(long id, String username, String email, String password) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
    }

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

    @Override
    public String toString() {
        return super.toString() + ", " +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", parola='" + password + '\'';
    }
}
