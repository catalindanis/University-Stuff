package repository;

import domain.Show;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowsDBRepository implements ShowsRepository {

    private final String TABLE_NAME = "shows";
    private final Logger logger = LogManager.getLogger();
    private final JdbcUtils jdbcUtils;

    public ShowsDBRepository(JdbcUtils jdbcUtils) {
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
            if (resultSet.next())
                result = resultSet.getInt(1);
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit(result);
        return result;
    }

    @Override
    public void save(Show entity) {
        logger.traceEntry();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("INSERT INTO %s (artist_name, date, location, remaining_seats) VALUES (?, ?, ?, ?)", TABLE_NAME)
            );

            statement.setString(1, entity.getArtistName());
            statement.setDate(2, java.sql.Date.valueOf(entity.getDate()));
            statement.setString(3, entity.getLocation());
            statement.setInt(4, entity.getRemainingSeats());
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
    public void update(Long aLong, Show entity) {
        logger.traceEntry();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("UPDATE %s SET artist_name = ?, date = ?, location = ?, remaining_seats = ? WHERE id = ?", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            statement.setString(1, entity.getArtistName());
            statement.setDate(2, java.sql.Date.valueOf(entity.getDate()));
            statement.setString(3, entity.getLocation());
            statement.setInt(4, entity.getRemainingSeats());
            statement.setLong(5, aLong);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit();

    }

    @Override
    public Show findOne(Long aLong) {
        logger.traceEntry();
        Show result = null;

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT * FROM %s WHERE id = ?", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                result = new Show(
                        resultSet.getLong("id"),
                        resultSet.getString("artist_name"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getString("location"),
                        resultSet.getInt("remaining_seats")
                );
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit(result);
        return result;
    }

    @Override
    public Iterable<Show> findAll() {
        logger.traceEntry();
        List<Show> result = new ArrayList<>();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT * FROM %s", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Show show = new Show(
                        resultSet.getLong("id"),
                        resultSet.getString("artist_name"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getString("location"),
                        resultSet.getInt("remaining_seats")
                );
                result.add(show);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit(result);
        return result;
    }
}
