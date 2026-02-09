package repositories;

import exceptions.FriendshipException;
import models.Friendship;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class DatabaseFriendshipsRepository extends FriendshipsRepository implements DatabaseRepository {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseFriendshipsRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        fetch();
    }

    @Override
    public Friendship add(Friendship friendship) {
        friendships.add(friendship);

        try {
            push();
        }
        catch (Exception e) {
            friendships.remove(friendship);
            throw new FriendshipException("Database error");
        }

        return friendship;
    }

    @Override
    public Friendship remove(Friendship friendship) {
        if(!friendships.remove(friendship))
            throw new FriendshipException("Friendship not found");

        try {
            push();
        }
        catch (Exception e) {
            friendships.add(friendship);
            throw new FriendshipException("Database error");
        }

        return friendship;
    }

    @Override
    public void fetch() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM \"Friendships\"");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long user1 = resultSet.getLong("user1");
                long user2 = resultSet.getLong("user2");

                friendships.add(new Friendship(
                        id,
                        user1,
                        user2
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void push() {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("DELETE FROM \"Friendships\"");
            statement.executeUpdate();

            for(Friendship friendship : friendships) {

                statement = connection.prepareStatement("INSERT INTO \"Friendships\" (id, user1, user2) VALUES (?, ?, ?)");

                statement.setLong(1, friendship.getId());
                statement.setLong(2, friendship.getUsers()[0]);
                statement.setLong(3, friendship.getUsers()[1]);

                statement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
