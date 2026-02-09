package repos;

import lombok.RequiredArgsConstructor;
import models.Entity;
import models.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepository<ID, E extends Entity<ID>> implements Repository<Integer, User> {
    private final String url;
    private final String username;
    private final String password;

    @Override
    public Optional<User> findById(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                var username = resultSet.getString("username");
                var createDate = resultSet.getTimestamp("create_date").toLocalDateTime();
                var modifyDate = resultSet.getTimestamp("modify_date").toLocalDateTime();
                var credits = resultSet.getInt("credits");

                return Optional.of(new User(username, createDate, modifyDate, credits));
            }

            return Optional.empty();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> save(User user) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("INSERT INTO users (username, create_date, modify_date, credit) VALUES (?, ?, ?, ?)");
            statement.setString(1, user.getUsername());
            statement.setTimestamp(2, Timestamp.valueOf(user.getCreateDate()));
            statement.setTimestamp(3, Timestamp.valueOf(user.getModifyDate()));
            statement.setInt(4, user.getCredits());

            var res = statement.executeUpdate();

            return res == 0 ? Optional.empty() : Optional.of(user);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> update(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> delete(Integer integer) {
        return Optional.empty();
    }
}
