package repositories;

import exceptions.UserException;
import models.*;
import services.GroupsService;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseUsersRepository extends UsersRepository implements DatabaseRepository {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseUsersRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        fetch();
    }

    @Override
    public User add(User user) {
        users.add(user);
        try {
            push();
        }
        catch (Exception e) {
            users.remove(user);
            System.out.println(e.getMessage());
            throw new UserException("Database error");
        }

        return user;
    }

    @Override
    public User remove(User user) {
        if(!users.remove(user))
            throw new UserException("User not found");

        try {
            push();
        }
        catch (Exception e) {
            users.add(user);
            throw new UserException("Database error");
        }

        return user;
    }

    @Override
    public void fetch() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM \"Users\"");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String type = resultSet.getString("type");
                if(type == null) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();
                    String occupation = resultSet.getString("occupation");
                    int empathyLevel = resultSet.getInt("empathy_level");

                    users.add(new Person(
                            id,
                            username,
                            email,
                            password,
                            firstName,
                            lastName,
                            dateOfBirth,
                            occupation,
                            empathyLevel
                    ));
                }
                else {
                    DuckType duckType = DuckType.valueOf(type);
                    double speed = resultSet.getDouble("speed");
                    double resistance = resultSet.getDouble("resistance");
                    long groupId = resultSet.getLong("group_id");

                    users.add( switch (duckType) {
                        case FLYING -> new FlyingDuck(
                                id,
                                username,
                                email,
                                password,
                                duckType,
                                speed,
                                resistance,
                                groupId
                        );
                        case SWIMMING -> new SwimmingDuck(
                                id,
                                username,
                                email,
                                password,
                                duckType,
                                speed,
                                resistance,
                                groupId
                        );
                        case FLYING_AND_SWIMMING -> new FlyingSwimmingDuck(
                                id,
                                username,
                                email,
                                password,
                                duckType,
                                speed,
                                resistance,
                                groupId
                        );
                    });
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void push() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("DELETE FROM \"Users\"");
            statement.executeUpdate();

            for(User user : users) {
                if(user instanceof Person) {
                    statement = connection.prepareStatement("INSERT INTO \"Users\" (" +
                            "id, " +
                            "username, " +
                            "email, " +
                            "password, " +
                            "first_name, " +
                            "last_name, " +
                            "date_of_birth, " +
                            "occupation, " +
                            "empathy_level" +
                            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

                    Person person = (Person) user;

                    statement.setLong(1, person.getId());
                    statement.setString(2, person.getUsername());
                    statement.setString(3, person.getEmail());
                    statement.setString(4, person.getPassword());
                    statement.setString(5, person.getFirstName());
                    statement.setString(6, person.getLastName());
                    statement.setDate(7, Date.valueOf(person.getDateOfBirth()));
                    statement.setString(8, person.getOccupation());
                    statement.setInt(9, person.getEmpathyLevel());

                    statement.executeUpdate();
                    continue;
                }
                statement = connection.prepareStatement("INSERT INTO \"Users\" (" +
                        "id, " +
                        "username, " +
                        "email, " +
                        "password, " +
                        "type, " +
                        "speed, " +
                        "resistance, " +
                        "group_id" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

                Duck duck = (Duck) user;

                statement.setLong(1, duck.getId());
                statement.setString(2, duck.getUsername());
                statement.setString(3, duck.getEmail());
                statement.setString(4, duck.getPassword());
                statement.setString(5, duck.getType().name());
                statement.setDouble(6, duck.getSpeed());
                statement.setDouble(7, duck.getResistance());
                if(duck.getGroup() != -1)
                    statement.setLong(8, duck.getGroup());
                else
                    statement.setNull(8, Types.NULL);

                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
