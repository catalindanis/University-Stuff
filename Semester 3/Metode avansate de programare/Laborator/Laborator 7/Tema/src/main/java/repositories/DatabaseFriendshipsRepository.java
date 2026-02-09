package repositories;

import dto.FriendshipsFilterDTO;
import dto.UsersFilterDTO;
import exceptions.FriendshipException;
import exceptions.RepositoryException;
import models.*;
import utils.Pair;
import utils.paging.Page;
import utils.paging.Pageable;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseFriendshipsRepository implements DatabaseRepository<Long, Friendship>, PageableRepository<Long, Friendship, FriendshipsFilterDTO> {
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
    public Friendship update(Long id, Friendship friendship) {
        return null;
    }

    private Pair<String, List<Object>> toSql(FriendshipsFilterDTO filter) {
        if (filter == null) {
            return new Pair<>("", Collections.emptyList());
        }
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        filter.getUser1().ifPresent(user1Filter -> {
            conditions.add("user1 = ?");
            params.add(user1Filter);
        });

        filter.getUser2().ifPresent(user2Filter -> {
            conditions.add("user2 = ?");
            params.add(user2Filter);
        });

        String sql = String.join(" and ", conditions);
        return new Pair<>(sql, params);
    }

    private int count(Connection connection, FriendshipsFilterDTO filter) throws SQLException {
        String sql = "select count(*) as count from \"Friendships\"";
        Pair<String, List<Object>> sqlFilter = toSql(filter);
        if (!sqlFilter.getFirst().isEmpty()) {
            sql += " where " + sqlFilter.getFirst();
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.getSecond()) {
                if(param != null)
                    statement.setObject(++paramIndex, param);
            }
            try (ResultSet result = statement.executeQuery()) {
                int elements = 0;
                if (result.next()) {
                    elements = result.getInt("count");
                }
                return elements;
            }
        }
    }

    @Override
    public Page<Friendship> getAllOnPage(Pageable pageable, FriendshipsFilterDTO filter) {
        List<Friendship> friendships = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "select * from \"Friendships\"";
            Pair<String, List<Object>> sqlFilter = toSql(filter);
            if (!sqlFilter.getFirst().isEmpty()) {
                sql += " where " + sqlFilter.getFirst();
            }
            sql += " limit ? offset ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                int paramIndex = 0;
                for (Object param : sqlFilter.getSecond()) {
                    if(param != null)
                        statement.setObject(++paramIndex, param);
                }
                statement.setInt(++paramIndex, pageable.getPageSize());
                statement.setInt(++paramIndex, pageable.getPageSize() * pageable.getPageNumber());

                try(ResultSet resultSet = statement.executeQuery()) {
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
                }
            }

            return new Page<Friendship>(friendships, count(connection, filter));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
