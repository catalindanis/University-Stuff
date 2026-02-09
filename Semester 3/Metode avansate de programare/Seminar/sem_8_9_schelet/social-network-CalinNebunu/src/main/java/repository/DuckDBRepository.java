package repository;

import domain.Duck;
import domain.FlyingDuck;
import domain.HybridDuck;
import domain.SwimmingDuck;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DuckDBRepository implements Repository<Long, Duck> {

    private String url;
    private String username;
    private String password;

    public DuckDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Duck> findOne(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT u.username, u.email, u.password, d.viteza, d.rezistenta, d.card_id, d.duck_type " +
                                                                        "FROM ducks d INNER JOIN users u ON d.id = u.id WHERE d.id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Daca a reusit, return duck
            if (resultSet.next()) {
                String username2 = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password2 = resultSet.getString("password");
                Double viteza = resultSet.getDouble("viteza");
                Double rezistenta = resultSet.getDouble("rezistenta");

                // Daca cardId e null, chiar returneaza null, nu 0
                Long cardId = resultSet.getObject("card_id", Long.class);

                String duckType = resultSet.getString("duck_type");
                Duck duck;

                switch (duckType) {
                    case "swimming" -> duck = new SwimmingDuck(username2, email, password2, viteza, rezistenta, cardId);
                    case "flying" -> duck = new FlyingDuck(username2, email, password2, viteza, rezistenta, cardId);
                    case "hybrid" -> duck = new HybridDuck(username2, email, password2, viteza, rezistenta, cardId);
                    default -> throw new RuntimeException("Unknown duck type: " + duckType);
                }
                duck.setId(id);
                return Optional.of(duck);

            }

            // Daca nu a gasit duck, return Optional.empty()
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Iterable<Duck> findAll() {

        Set<Duck> ducks = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT u.id, u.username, u.email, u.password, d.viteza, d.rezistenta, d.card_id, d.duck_type " +
                                                                         "FROM ducks d INNER JOIN users u ON d.id = u.id");
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username2 = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password2 = resultSet.getString("password");
                Double viteza = resultSet.getDouble("viteza");
                Double rezistenta = resultSet.getDouble("rezistenta");

                // Daca cardId e null, chiar returneaza null, nu 0
                Long cardId = resultSet.getObject("card_id", Long.class);

                String duckType = resultSet.getString("duck_type");
                Duck duck;

                switch (duckType) {
                    case "swimming" -> duck = new SwimmingDuck(username2, email, password2, viteza, rezistenta, cardId);
                    case "flying" -> duck = new FlyingDuck(username2, email, password2, viteza, rezistenta, cardId);
                    case "hybrid" -> duck = new HybridDuck(username2, email, password2, viteza, rezistenta, cardId);
                    default -> throw new RuntimeException("Unknown duck type: " + duckType);
                }

                duck.setId(id);
                ducks.add(duck);
            }
            return ducks;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Duck> save(Duck entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Tranzactie, pentru a nu fi jumatate de duck adaugata
            connection.setAutoCommit(false);

            // 1. Inseram prima data in users
            String insertUserSQL = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement psUser = connection.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, entity.getUsername());
                psUser.setString(2, entity.getEmail());
                psUser.setString(3, entity.getPassword());

                // Daca nu a reusit save, return entity
                int userInserted = psUser.executeUpdate();
                if (userInserted == 0) {
                    connection.rollback();
                    return Optional.of(entity);
                }

                // Obtinem id-ul generat de DB
                ResultSet generatedKeys = psUser.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    entity.setId(id);

                    // 2. Inseram in tabela ducks
                    String insertDuckSQL = "INSERT INTO ducks (id, viteza, rezistenta, card_id, duck_type) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement psDuck = connection.prepareStatement(insertDuckSQL)) {
                        psDuck.setLong(1, id);
                        psDuck.setDouble(2, entity.getViteza());
                        psDuck.setDouble(3, entity.getRezistenta());

                        if (entity.getCardId() != null) {
                            psDuck.setLong(4, entity.getCardId());
                        } else {
                            psDuck.setNull(4, Types.BIGINT);
                        }

                        psDuck.setString(5, entity.getDuckType());

                        // Daca ceva nu a reusit, return entity
                        int duckInserted = psDuck.executeUpdate();
                        if (duckInserted == 0) {
                            connection.rollback();
                            return Optional.of(entity);
                        }
                    }

                    // Daca a reusit, dam commit si return Optional.empty()
                    connection.commit();
                    return Optional.empty();

                // Nu s-a generat id, rollback si return entity
                } else {
                    connection.rollback();
                    return Optional.of(entity);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Duck> delete(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        String deleteDuckSQL = "DELETE FROM ducks WHERE id = ?";

        // Optional daca avem ON DELETE CASCADE
        String deleteUserSQL = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Tranzactie
            connection.setAutoCommit(false);

            Optional<Duck> foundDuck = findOne(id);

            if (foundDuck.isPresent()) {

                // Stergem prima oara duck
                try (PreparedStatement psDuck = connection.prepareStatement(deleteDuckSQL)) {
                    psDuck.setLong(1, id);
                    psDuck.executeUpdate();
                }

                // Optional: stergem user-ul asociat daca nu avem ON DELETE CASCADE
                try (PreparedStatement psUser = connection.prepareStatement(deleteUserSQL)) {
                    psUser.setLong(1, id);
                    psUser.executeUpdate();
                }

                // Daca a reusit, commit si return foundDuck
                connection.commit();
                return foundDuck;

            // Daca nu a reusit, adica nu era nimic de sters, return Optional.empty()
            } else {
                connection.rollback();
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Duck> update(Duck entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        String updateUserSQL = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        String updateDuckSQL = "UPDATE ducks SET viteza = ?, rezistenta = ?, card_id = ?, duck_type = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Tranzactia
            connection.setAutoCommit(false);

            try (PreparedStatement psUser = connection.prepareStatement(updateUserSQL);
                PreparedStatement psDuck = connection.prepareStatement(updateDuckSQL)) {

                // 1. Update User
                psUser.setString(1, entity.getUsername());
                psUser.setString(2, entity.getEmail());
                psUser.setString(3, entity.getPassword());
                psUser.setLong(4, entity.getId());
                psUser.executeUpdate();

                // 2. Update Duck
                psDuck.setDouble(1, entity.getViteza());
                psDuck.setDouble(2, entity.getRezistenta());

                // Folosim setObject ca poate fi null
                psDuck.setObject(3, entity.getCardId(), Types.BIGINT);
                
                psDuck.setString(4, entity.getDuckType());
                psDuck.setLong(5, entity.getId());

                int rowsAffected = psDuck.executeUpdate();

                // Salvam ambele update-uri
                connection.commit();

                // Daca a reusit, return Optional.empty(). Daca nu a reusit, return entity
                return rowsAffected > 0 ? Optional.empty() : Optional.of(entity);

            // Daca ceva nu merge, revenim la starea initiala
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
