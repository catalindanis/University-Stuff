package repositories;

import exceptions.GroupException;
import exceptions.RepositoryException;
import models.*;
import services.UsersService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseGroupsRepository implements DatabaseRepository<Long, Group> {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseGroupsRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Group add(Group group) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Groups\" (" +
                    "name, " +
                    "type" +
                    ") VALUES (?, ?) RETURNING id");

            statement.setString(1, group.getName());
            statement.setString(2, group.getDucksType().name());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                long generatedId = rs.getLong(1);
                group.setId(generatedId);
            }

            return group;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Group remove(Group group) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("DELETE FROM \"Groups\" WHERE id = ?");

            statement.setLong(1, group.getId());

            int response = statement.executeUpdate();
            return response != 0 ? group : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Group get(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM \"Groups\" WHERE id = ?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");

                return switch(DuckType.valueOf(type)) {
                            case FLYING -> new FlyingGroup(id, name,
                                    UsersService.getInstance().getDucks().stream().filter(duck -> duck.getGroup() == id).collect(Collectors.toList())
                            );
                            case SWIMMING -> new SwimmingGroup(id, name,
                                    UsersService.getInstance().getDucks().stream().filter(duck -> duck.getGroup() == id).collect(Collectors.toList())
                            );
                            case FLYING_AND_SWIMMING -> new FlyingSwimmingGroup(id, name,
                                    UsersService.getInstance().getDucks().stream().filter(duck -> duck.getGroup() == id).collect(Collectors.toList())
                            );
                };
            }

            throw new GroupException("Card negasit");
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<Group> getAll() {
        List<Group> groups = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM \"Groups\"");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");

                groups.add(
                        switch(DuckType.valueOf(type)) {
                            case FLYING -> new FlyingGroup(id, name,
                                    UsersService.getInstance().getDucks().stream().filter(duck -> duck.getGroup() == id).collect(Collectors.toList())
                            );
                            case SWIMMING -> new SwimmingGroup(id, name,
                                    UsersService.getInstance().getDucks().stream().filter(duck -> duck.getGroup() == id).collect(Collectors.toList())
                            );
                            case FLYING_AND_SWIMMING -> new FlyingSwimmingGroup(id, name,
                                    UsersService.getInstance().getDucks().stream().filter(duck -> duck.getGroup() == id).collect(Collectors.toList())
                            );
                        }
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return groups;
    }

    @Override
    public Group update(Long aLong, Group group) {
        return null;
    }
}

