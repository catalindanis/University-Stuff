package repository;

import domain.Show;
import domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketsDBRepository implements TicketsRepository {

    private final String TABLE_NAME = "tickets";
    private final Logger logger = LogManager.getLogger();
    private final JdbcUtils jdbcUtils;

    public TicketsDBRepository(JdbcUtils jdbcUtils) {
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
    public void save(Ticket entity) {
        logger.traceEntry();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("INSERT INTO %s (client_name, show_id, no_seats) VALUES (?, ?, ?)", TABLE_NAME)
            );

            statement.setString(1, entity.getClientName());
            statement.setLong(2, entity.getShow().getId());
            statement.setInt(3, entity.getNoSeats());
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
    public void update(Long aLong, Ticket entity) {
        logger.traceEntry();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("UPDATE %s SET client_name = ?, show_id = ?, no_seats = ? WHERE id = ?", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            statement.setString(1, entity.getClientName());
            statement.setLong(2, entity.getShow().getId());
            statement.setInt(3, entity.getNoSeats());
            statement.setLong(4, aLong);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit();
    }

    @Override
    public Ticket findOne(Long aLong) {
        logger.traceEntry();
        Ticket result = null;

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT t.id, t.client_name, t.no_seats, " +
                            "s.id AS show_id, s.artist_name, s.date, s.location, s.remaining_seats " +
                            "FROM %s t JOIN shows s ON t.show_id = s.id WHERE t.id = ?", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                result = buildTicketFromJoinResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit(result);
        return result;
    }

    @Override
    public Iterable<Ticket> findAll() {
        logger.traceEntry();
        List<Ticket> result = new ArrayList<>();

        try (Connection connection = jdbcUtils.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT t.id, t.client_name, t.no_seats, " +
                            "s.id AS show_id, s.artist_name, s.date, s.location, s.remaining_seats " +
                            "FROM %s t JOIN shows s ON t.show_id = s.id", TABLE_NAME)
            );
            logger.info("Executing: " + statement);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(buildTicketFromJoinResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        logger.traceExit(result);
        return result;
    }

    private Ticket buildTicketFromJoinResultSet(ResultSet resultSet) throws SQLException {
        Show show = new Show(
                resultSet.getLong("show_id"),
                resultSet.getString("artist_name"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getString("location"),
                resultSet.getInt("remaining_seats")
        );

        return new Ticket(
                resultSet.getLong("id"),
                resultSet.getString("client_name"),
                show,
                resultSet.getInt("no_seats")
        );
    }
}
