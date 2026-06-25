package ro.mpp2026.festivalmuzicajavafx.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT COUNT(*) FROM %s", TABLE_NAME)
            );
            logger.info("Executing: {}", statement);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                result = resultSet.getInt(1);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        logger.traceExit(result);
        return result;
    }

    @Override
    public Long save(Show entity) {
        logger.traceEntry();
        Long result = null;
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("INSERT INTO %s (artist_name, date, location, remaining_seats) VALUES (?, ?, ?, ?)", TABLE_NAME),
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, entity.getArtistName());
            statement.setDate(2, java.sql.Date.valueOf(entity.getDate()));
            statement.setString(3, entity.getLocation());
            statement.setInt(4, entity.getRemainingSeats());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                result = generatedKeys.getLong(1);
                entity.setId(result);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit(result);
        return result;
    }

    @Override
    public void delete(Long aLong) {
        logger.traceEntry();
        Connection connection = jdbcUtils.getConnection();
        try {
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
        Connection connection = jdbcUtils.getConnection();
        try {
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
        Connection connection = jdbcUtils.getConnection();
        try {
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
        Connection connection = jdbcUtils.getConnection();
        try {
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

    @Override
    public Iterable<Show> findAll(ShowFilter filter) {
        if(filter == null)
            return findAll();

        logger.traceEntry();
        List<Show> result = new ArrayList<>();
        Connection connection = jdbcUtils.getConnection();

        StringBuilder sql = new StringBuilder("SELECT * FROM " + TABLE_NAME + " WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (filter.artistName() != null && !filter.artistName().isEmpty()) {
            sql.append(" AND artist_name LIKE ?");
            params.add("%" + filter.artistName() + "%");
        }
        if (filter.date() != null) {
            sql.append(" AND date = ?");
            params.add(java.sql.Date.valueOf(filter.date()));
        }
        if (filter.location() != null && !filter.location().isEmpty()) {
            sql.append(" AND location LIKE ?");
            params.add("%" + filter.location() + "%");
        }
        if (filter.remainingSeats() != null) {
            sql.append(" AND remaining_seats = ?");
            params.add(filter.remainingSeats());
        }

        try {
            PreparedStatement statement = connection.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
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
