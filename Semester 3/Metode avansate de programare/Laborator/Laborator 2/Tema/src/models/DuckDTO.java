package models;

public class DuckDTO {
    public String username;
    public String email;
    public String password;
    public DuckType type;
    public double speed;
    public double resistance;
    public Group group;

    public DuckDTO() {}

    public DuckDTO(String username, String email, String password, DuckType type, double speed, double resistance, Group group) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.type = type;
        this.speed = speed;
        this.resistance = resistance;
        this.group = group;
    }
}
