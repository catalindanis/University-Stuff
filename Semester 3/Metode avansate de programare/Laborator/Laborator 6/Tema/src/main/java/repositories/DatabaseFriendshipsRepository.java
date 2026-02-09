package repositories;

import exceptions.FriendshipException;
import exceptions.RepositoryException;
import models.Friendship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFriendshipsRepository implements DatabaseRepository<Long, Friendship> {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseFriendshipsRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship add(Friendship friendship) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Friendships\" (" +
                    "user1, " +
                    "user2" +
                    ") VALUES (?, ?)");

            statement.setLong(1, friendship.getUsers()[0]);
            statement.setLong(2, friendship.getUsers()[1]);

            statement.executeUpdate();

            return friendship;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Friendship remove(Friendship friendship) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("DELETE FROM \"Friendships\" WHERE id = ?");

            statement.setLong(1, friendship.getId());

            int response = statement.executeUpdate();
            return response != 0 ? friendship : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Friendship get(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM \"Friendships\" WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                long user1 = resultSet.getLong("user1");
                long user2 = resultSet.getLong("user2");

                return new Friendship(
                        id,
                        user1,
                        user2
                );
            }

            throw new FriendshipException("Prietenie negasita");
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<Friendship> getAll() {
        List<Friendship> friendships = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM \"Friendships\"");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Long user1 = resultSet.getLong("user1");
                Long user2 = resultSet.getLong("user2");

                friendships.add(new Friendship(
                            id,
                            user1,
                            user2
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return friendships;
    }

    @Override
    public Friendship update(Long aLong, Friendship friendship) {
        return null;
    }
}
