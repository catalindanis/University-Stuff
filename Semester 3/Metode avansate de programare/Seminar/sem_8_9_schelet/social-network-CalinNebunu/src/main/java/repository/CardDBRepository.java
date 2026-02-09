package repository;

import domain.Card;
import utils.paging.Page;
import utils.paging.Pageable;

import java.sql.*;
import java.util.*;

public class CardDBRepository implements Repository<Long, Card> {

    private String url;
    private String username;
    private String password;

    public CardDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Card> findOne(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM cards WHERE id = ?")) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Daca a reusit, return cardul
            if (resultSet.next()) {
                String numeCard = resultSet.getString("nume_card");
                String tipMembri = resultSet.getString("tip_membri");
                Card card = new Card(numeCard, tipMembri);
                card.setId(id);
                return Optional.of(card);
            }

            // Daca nu a reusit, return Optional.empty()
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Iterable<Card> findAll() {

        Set<Card> cards = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM cards");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String numeCard = resultSet.getString("nume_card");
                String tipMembri = resultSet.getString("tip_membri");
                Card card = new Card(numeCard, tipMembri);
                card.setId(id);
                cards.add(card);
            }
            return cards;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Card> save(Card entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        String insertSQL = "INSERT INTO cards (nume_card, tip_membri) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            statement.setString(1, entity.getNumeCard());
            statement.setString(2, entity.getTipMembri());

            // Daca a reusit, return Optional.empty(). Daca nu a reusit, return entity
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0 ? Optional.empty(): Optional.of(entity);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Card> delete(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        Optional<Card> foundCard = findOne(id);
        if (foundCard.isEmpty()) return Optional.empty();

        String deleteSQL = "DELETE FROM cards WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(deleteSQL)) {

            statement.setLong(1, id);

            // Daca a reusit, return foundCard, cel sters. Daca nu a reusit, return Optional.empty()
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0 ? foundCard : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Card> update(Card entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        String updateSQL = "UPDATE cards SET nume_card = ?, tip_membri = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(updateSQL)) {

            statement.setString(1, entity.getNumeCard());
            statement.setString(2, entity.getTipMembri());
            statement.setLong(3, entity.getId());

            // Daca a reusit, return Optional.empty(). Daca nu a reusit, return entity
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0 ? Optional.empty() : Optional.of(entity);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public int count() {
        String sql = "SELECT COUNT(*) AS total FROM cards";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    private Page<Card> findAllOnPage(Connection connection, Pageable pageable) throws SQLException {

        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM cards LIMIT ? OFFSET ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, pageable.getPageSize());
            ps.setInt(2, pageable.getPageSize() * pageable.getPageNumber());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String numeCard = rs.getString("nume_card");
                String tipMembri = rs.getString("tip_membri");

                Card c = new Card(numeCard, tipMembri);
                c.setId(id);

                cards.add(c);
            }
        }

        int totalCards = count();

        return new Page<>(cards, totalCards);
    }

    public Page<Card> findAllOnPage(Pageable pageable) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            return findAllOnPage(connection, pageable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
