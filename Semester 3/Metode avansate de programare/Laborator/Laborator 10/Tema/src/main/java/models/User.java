package models;

import utils.Observer;

import java.io.Serial;
import java.io.Serializable;

public abstract class User extends Entity<Long> implements Observer<Event>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    protected String username;
    protected String email;
    protected String password;
    protected UserType userType;

    public User(long id, String username, String email, String password) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = UserType.user;
    }

    public User(long id, String username, String email, String password, UserType userType) {
        super(id);
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
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

    public UserType getUserType() {
        return userType;
    }

    @Override
    public void update(Event event) {
//        System.out.println(username + " a fost notificat de eveniment #" + event.getId());
    }

    @Override
    public String toString() {
        return super.toString() + ", " +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", parola='" + password + '\'';
    }
}
