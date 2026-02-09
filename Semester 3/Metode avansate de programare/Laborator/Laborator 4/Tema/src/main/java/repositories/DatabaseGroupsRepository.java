package repositories;

import exceptions.UserException;
import models.*;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseGroupsRepository extends GroupsRepository implements DatabaseRepository {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseGroupsRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        fetch();
    }

    @Override
    public void fetch() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM \"Groups\"");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                DuckType duckType = DuckType.valueOf(type);

                groups.add(
                        switch(duckType) {
                            case SWIMMING -> new SwimmingGroup(
                                    id,
                                    name,
                            );
                            case FLYING -> new FlyingGroup(
                                    id,
                                    name,
                            );
                            case FLYING_AND_SWIMMING -> new FlyingSwimmingGroup(
                                    id,
                                    name
                            );
                        }
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void push() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("DELETE FROM \"Groups\"");
            statement.executeUpdate();

            for(Group group : groups) {
                statement = connection.prepareStatement("INSERT INTO \"Groups\" (" +
                        "id, " +
                        "name, " +
                        ") VALUES (?, ?)");

                statement.setLong(1, group.getId());
                statement.setString(2, group.getName());

                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Group add(Group group) {
        groups.add(group);
        try {
            push();
        }
        catch (Exception e) {
            groups.remove(group);
            System.out.println(e.getMessage());
            throw new UserException("Database error");
        }

        return group;
    }

    @Override
    public Group remove(Group group) {
        if(!groups.remove(group))
            throw new UserException("Group not found");

        try {
            push();
        }
        catch (Exception e) {
            groups.add(group);
            throw new UserException("Database error");
        }

        return group;
    }
}
