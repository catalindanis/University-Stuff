package repository;

import domain.Friendship;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class FriendshipDBRepository implements FriendshipRepository {

    private String url;
    private String username;
    private String password;

    public FriendshipDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Friendship> save(Friendship friendship) {

        if (friendship == null) {
            throw new IllegalArgumentException("Friendship cannot be null");
        }

        String insertSQL = "INSERT INTO friendships (user1_id, user2_id) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            statement.setLong(1, friendship.getUser1Id());
            statement.setLong(2, friendship.getUser2Id());

            // Daca a reusit, return Optional.empty(). Daca nu a reusit, return friendship
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0 ? Optional.empty(): Optional.of(friendship);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Friendship> delete(Friendship friendship) {

        if (friendship == null) {
            throw new IllegalArgumentException("Friendship cannot be null");
        }

        String deleteSQL = "DELETE FROM friendships WHERE user1_id = ? AND user2_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {

            statement.setLong(1, friendship.getUser1Id());
            statement.setLong(2, friendship.getUser2Id());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0 ? Optional.of(friendship) : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public Iterable<Friendship> findAllFriendsOf(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }


        Set<Friendship> friends = new HashSet<>();
        String findSQL = "SELECT * FROM friendships WHERE user1_id = ? OR user2_id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(findSQL)) {

            statement.setLong(1, id);
            statement.setLong(2, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long user1Id = resultSet.getLong("user1_id");
                Long user2Id = resultSet.getLong("user2_id");

                Friendship friendship = new Friendship(user1Id, user2Id);
                Long idFriendship = resultSet.getLong("id");
                friendship.setId(idFriendship);

                friends.add(friendship);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return friends;

    }
}
