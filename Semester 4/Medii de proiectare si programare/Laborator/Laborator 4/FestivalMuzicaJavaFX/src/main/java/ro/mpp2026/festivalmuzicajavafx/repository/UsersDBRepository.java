package ro.mpp2026.festivalmuzicajavafx.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UsersDBRepository implements UsersRepository {

    private final String TABLE_NAME = "users";
    private final Logger logger = LogManager.getLogger();
    private final JdbcUtils jdbcUtils;

    public UsersDBRepository(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public int size() {
        logger.traceEntry();
        int result = 0;

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT COUNT(*) FROM %s", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                result = resultSet.getInt(1);
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit(result);
        return result;
    }

    @Override
    public void save(User entity) {
        logger.traceEntry();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("INSERT INTO %s (email, password) VALUES (?, ?)", TABLE_NAME)
            );

            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit();
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            statement.setLong(1, aLong);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit();
    }

    @Override
    public void update(Long aLong, User entity) {
        logger.traceEntry();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("UPDATE %s SET email = ?, password = ? WHERE id = ?", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getPassword());
            statement.setLong(3, aLong);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit();
    }

    @Override
    public User findOne(Long aLong) {
        logger.traceEntry();
        User result = null;

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT * FROM %s WHERE id = ?", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit(result);
        return result;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry();
        java.util.List<User> result = new java.util.ArrayList<>();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT * FROM %s", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
                result.add(user);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit(result);
        return result;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        logger.traceEntry();
        User result = null;

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT * FROM %s WHERE email = ? and password = ?", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("email"),
                        resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit(result);
        return Optional.ofNullable(result);
    }
}
