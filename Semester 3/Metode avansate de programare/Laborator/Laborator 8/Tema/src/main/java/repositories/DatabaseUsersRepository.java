package repositories;

import dto.UsersFilterDTO;
import exceptions.RepositoryException;
import exceptions.UserException;
import models.*;
import utils.Pair;
import utils.paging.Page;
import utils.paging.Pageable;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseUsersRepository implements DatabaseRepository<Long, User>, PageableRepository<Long, User, UsersFilterDTO> {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseUsersRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public User add(User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
                if(user instanceof Person) {
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Users\" (" +
                            "username, " +
                            "email, " +
                            "password, " +
                            "first_name, " +
                            "last_name, " +
                            "date_of_birth, " +
                            "occupation, " +
                            "empathy_level" +
                            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

                    Person person = (Person) user;

                    statement.setString(1, person.getUsername());
                    statement.setString(2, person.getEmail());
                    statement.setString(3, person.getPassword());
                    statement.setString(4, person.getFirstName());
                    statement.setString(5, person.getLastName());
                    statement.setDate(6, Date.valueOf(person.getDateOfBirth()));
                    statement.setString(7, person.getOccupation());
                    statement.setInt(8, person.getEmpathyLevel());

                    statement.executeUpdate();

                    return person;
                }
                PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Users\" (" +
                        "username, " +
                        "email, " +
                        "password, " +
                        "type, " +
                        "speed, " +
                        "resistance, " +
                        "group_id" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?)");

                Duck duck = (Duck) user;

                statement.setString(1, duck.getUsername());
                statement.setString(2, duck.getEmail());
                statement.setString(3, duck.getPassword());
                statement.setString(4, duck.getType().name());
                statement.setDouble(5, duck.getSpeed());
                statement.setDouble(6, duck.getResistance());
                if(duck.getGroup() != -1)
                    statement.setLong(7, duck.getGroup());
                else
                    statement.setNull(7, Types.NULL);

                statement.executeUpdate();

                return duck;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User remove(User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("DELETE FROM \"Users\" WHERE id = ?");

            statement.setLong(1, user.getId());

            int response = statement.executeUpdate();
            return response != 0 ? user : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User update(Long id, User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            PreparedStatement select = connection.prepareStatement(
                    "SELECT id FROM \"Users\" WHERE id = ?"
            );
            select.setLong(1, id);
            var rs = select.executeQuery();
            if (!rs.next()) {
                return null;
            }

            if (user instanceof Person) {
                PreparedStatement updatePerson = connection.prepareStatement(
                        "UPDATE \"Users\" SET " +
                                "username = ?, " +
                                "email = ?, " +
                                "password = ?, " +
                                "first_name = ?, " +
                                "last_name = ?, " +
                                "date_of_birth = ?, " +
                                "occupation = ?, " +
                                "empathy_level = ? " +
                                "WHERE id = ?"
                );

                Person person = (Person) user;

                updatePerson.setString(1, person.getUsername());
                updatePerson.setString(2, person.getEmail());
                updatePerson.setString(3, person.getPassword());
                updatePerson.setString(4, person.getFirstName());
                updatePerson.setString(5, person.getLastName());
                updatePerson.setDate(6, Date.valueOf(person.getDateOfBirth()));
                updatePerson.setString(7, person.getOccupation());
                updatePerson.setInt(8, person.getEmpathyLevel());
                updatePerson.setLong(9, id);

                int updated = updatePerson.executeUpdate();
                return updated > 0 ? user : null;
            }

            PreparedStatement updateDuck = connection.prepareStatement(
                    "UPDATE \"Users\" SET " +
                            "username = ?, " +
                            "email = ?, " +
                            "password = ?, " +
                            "type = ?, " +
                            "speed = ?, " +
                            "resistance = ?, " +
                            "group_id = ? " +
                            "WHERE id = ?"
            );

            Duck duck = (Duck) user;

            updateDuck.setString(1, duck.getUsername());
            updateDuck.setString(2, duck.getEmail());
            updateDuck.setString(3, duck.getPassword());
            updateDuck.setString(4, duck.getType().name());
            updateDuck.setDouble(5, duck.getSpeed());
            updateDuck.setDouble(6, duck.getResistance());
            if (duck.getGroup() != -1)
                updateDuck.setLong(7, duck.getGroup());
            else
                updateDuck.setNull(7, Types.NULL);
            updateDuck.setLong(8, id);

            int updated = updateDuck.executeUpdate();
            return updated > 0 ? user : null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User get(Long id) {
        try(Connection connection= DriverManager.getConnection(url,username,password)) {
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Users\" WHERE id = ?")) {
                statement.setLong(1, id);
                ResultSet resultSet=statement.executeQuery();

                if(resultSet.next()) {
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

                        return new Person(
                                id,
                                username,
                                email,
                                password,
                                firstName,
                                lastName,
                                dateOfBirth,
                                occupation,
                                empathyLevel
                        );
                    }
                    else {
                        DuckType duckType = DuckType.valueOf(type);
                        double speed = resultSet.getDouble("speed");
                        double resistance = resultSet.getDouble("resistance");
                        long groupId = resultSet.getLong("group_id");

                        return switch (duckType) {
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
                        };
                    }
                }

                throw new UserException("Utilizator negasit");
            }
        } catch(SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

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
        return users;
    }

    private Pair<String, List<Object>> toSql(UsersFilterDTO filter) {
        if (filter == null) {
            return new Pair<>("", Collections.emptyList());
        }
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        filter.getUsername().ifPresent(usernameFilter -> {
            conditions.add("username like ?");
            params.add("%" + usernameFilter + "%");
        });

        filter.getLoginAction().ifPresentOrElse(
            loginActionFilter -> {
                filter.getEmail().ifPresent(emailFilter -> {
                    conditions.add("email like ?");
                    params.add(emailFilter);
                });

                filter.getPassword().ifPresent(passwordFilter -> {
                    conditions.add("password like ?");
                    params.add(passwordFilter);
                });
            },
            () -> {
                filter.getEmail().ifPresent(emailFilter -> {
                    conditions.add("email like ?");
                    params.add("%" + emailFilter + "%");
                });

                filter.getPassword().ifPresent(passwordFilter -> {
                    conditions.add("password like ?");
                    params.add("%" + passwordFilter + "%");
                });

                if(filter.getUserType().isPresent() && filter.getUserType().get() == Person.class) {
                    conditions.add("type IS NULL");
                    params.add(null);
                }
                else {
                    conditions.add("type IS NOT NULL");
                    params.add(null);

                    filter.getType()
                            .filter(typeFilter -> typeFilter != null)
                            .ifPresent(typeFilter -> {
                                conditions.add("type like ?");
                                params.add(typeFilter.name());
                            });
                }
            }
        );

        filter.getFirstName().ifPresent(firstNameFilter -> {
            conditions.add("first_name like ?");
            params.add("%" + firstNameFilter + "%");
        });

        filter.getLastName().ifPresent(lastNameFilter -> {
            conditions.add("last_name like ?");
            params.add("%" + lastNameFilter + "%");
        });

        filter.getDateOfBirth().ifPresent(dateOfBirthFilter -> {
            conditions.add("date_of_birth = ?");
            params.add(dateOfBirthFilter);
        });

        filter.getOccupation().ifPresent(occupationFilter -> {
            conditions.add("occupation like ?");
            params.add(occupationFilter);
        });

        filter.getEmpathyLevel().ifPresent(empathyLevelFilter -> {
            conditions.add("empathy_level = ?");
            params.add(empathyLevelFilter);
        });

        filter.getSpeed().ifPresent(speedFilter -> {
            conditions.add("speed = ?");
            params.add(speedFilter);
        });

        filter.getResistance().ifPresent(resistanceFilter -> {
            conditions.add("resistance = ?");
            params.add(resistanceFilter);
        });

        filter.getGroup().ifPresent(groupFilter -> {
            conditions.add("group_id = ?");
            params.add(groupFilter);
        });

        String sql = String.join(" and ", conditions);
        return new Pair<>(sql, params);
    }

    private int count(Connection connection, UsersFilterDTO filter) throws SQLException {
        String sql = "select count(*) as count from \"Users\"";
        Pair<String, List<Object>> sqlFilter = toSql(filter);
        if (!sqlFilter.getFirst().isEmpty()) {
            sql += " where " + sqlFilter.getFirst();
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.getSecond()) {
                if(param != null)
                    statement.setObject(++paramIndex, param);
            }
            try (ResultSet result = statement.executeQuery()) {
                int elements = 0;
                if (result.next()) {
                    elements = result.getInt("count");
                }
                return elements;
            }
        }
    }

    @Override
    public Page<User> getAllOnPage(Pageable pageable, UsersFilterDTO filter) {
        List<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "select * from \"Users\"";
            Pair<String, List<Object>> sqlFilter = toSql(filter);
            if (!sqlFilter.getFirst().isEmpty()) {
                sql += " where " + sqlFilter.getFirst();
            }
            sql += " limit ? offset ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int paramIndex = 0;
                for (Object param : sqlFilter.getSecond()) {
                    if(param != null)
                        statement.setObject(++paramIndex, param);
                }
                statement.setInt(++paramIndex, pageable.getPageSize());
                statement.setInt(++paramIndex, pageable.getPageSize() * pageable.getPageNumber());

                try(ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String username = resultSet.getString("username");
                        String email = resultSet.getString("email");
                        String password = resultSet.getString("password");
                        String type = resultSet.getString("type");
                        if (type == null) {
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
                        } else {
                            DuckType duckType = DuckType.valueOf(type);
                            double speed = resultSet.getDouble("speed");
                            double resistance = resultSet.getDouble("resistance");
                            long groupId = resultSet.getLong("group_id");

                            users.add(switch (duckType) {
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
                }
            }

            return new Page<User>(users, count(connection, filter));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
