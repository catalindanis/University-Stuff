package repository;

import domain.Persoana;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PersoanaDBRepository implements Repository<Long, Persoana> {

    private String url;
    private String username;
    private String password;

    public PersoanaDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Persoana> findOne(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT u.username, u.email, u.password, p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie " +
                                                                        "FROM persoane p INNER JOIN users u ON p.id = u.id WHERE p.id = ?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Daca a reusit, returnam persoana
            if (resultSet.next()) {
                String username2 = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password2 = resultSet.getString("password");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                LocalDate dataNasterii = resultSet.getDate("data_nasterii").toLocalDate();
                String ocupatie = resultSet.getString("ocupatie");
                Integer nivelEmpatie =  resultSet.getInt("nivel_empatie");

                Persoana persoana = new Persoana(username2, email, password2, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
                persoana.setId(id);
                return Optional.of(persoana);
            }

            // Daca nu a reusit, returnam Optional.empty()
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Iterable<Persoana> findAll() {

        Set<Persoana> persoane = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT u.id, u.username, u.email, u.password, p.nume, p.prenume, p.data_nasterii, p.ocupatie, p.nivel_empatie " +
                                                                          "FROM persoane p INNER JOIN users u ON p.id = u.id");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username2 = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password2 = resultSet.getString("password");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                LocalDate dataNasterii = resultSet.getDate("data_nasterii").toLocalDate();
                String ocupatie = resultSet.getString("ocupatie");
                Integer nivelEmpatie =  resultSet.getInt("nivel_empatie");

                Persoana persoana = new Persoana(username2, email, password2, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
                persoana.setId(id);
                persoane.add(persoana);
            }
            return persoane;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Persoana> save(Persoana entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Tranzactie, pentru a nu fi jumatate de persoana adaugata
            connection.setAutoCommit(false);

            // 1. Inseram prima data in users
            String insertUserSQL = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement psUser = connection.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, entity.getUsername());
                psUser.setString(2, entity.getEmail());
                psUser.setString(3, entity.getPassword());

                // Daca ceva nu a reusit, returnam entity
                int userInserted = psUser.executeUpdate();
                if (userInserted == 0) {
                    connection.rollback();
                    return Optional.of(entity);
                }

                // Obtinem id-ul generat de DB
                ResultSet generatedKeys = psUser.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    entity.setId(id);

                    // 2. Inseram in tabela persoane
                    String insertPersoanaSQL = "INSERT INTO persoane (id, nume, prenume, data_nasterii, ocupatie, nivel_empatie) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement psPersoana = connection.prepareStatement(insertPersoanaSQL)) {
                        psPersoana.setLong(1, id);
                        psPersoana.setString(2, entity.getNume());
                        psPersoana.setString(3, entity.getPrenume());
                        psPersoana.setDate(4, Date.valueOf(entity.getDataNasterii()));
                        psPersoana.setString(5, entity.getOcupatie());
                        psPersoana.setInt(6, entity.getNivelEmpatie());

                        // Daca ceva nu a reusit, returnam entity
                        int persoanaInserted = psPersoana.executeUpdate();
                        if (persoanaInserted == 0) {
                            connection.rollback();
                            return Optional.of(entity);
                        }
                    }

                    // Daca inserarea a reusit, commit si return Optional.empty()
                    connection.commit();
                    return Optional.empty();

                // Daca inserarea nu a reusit, adica nu s-a generat id, return entity
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
    public Optional<Persoana> delete(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        String deletePersoanaSQL = "DELETE FROM persoane WHERE id = ?";

        // Optional daca avem ON DELETE CASCADE
        String deleteUserSQL = "DELETE FROM users WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Tranzactie
            connection.setAutoCommit(false);

            Optional<Persoana> foundPersoana = findOne(id);

            if (foundPersoana.isPresent()) {

                // Stergem prima data persoana
                try (PreparedStatement psPersoana = connection.prepareStatement(deletePersoanaSQL)) {
                    psPersoana.setLong(1, id);
                    psPersoana.executeUpdate();
                }

                // Optional: stergem user-ul asociat daca nu avem ON DELETE CASCADE
                try (PreparedStatement psUser = connection.prepareStatement(deleteUserSQL)) {
                    psUser.setLong(1, id);
                    psUser.executeUpdate();
                }

                // Daca a reusit, commit si return foundPersoana
                connection.commit();
                return foundPersoana;

            // Daca nu a reusit, return Optional.empty()
            } else {
                connection.rollback();
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Persoana> update(Persoana entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        String updateUserSQL = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        String updatePersoanaSQL = "UPDATE persoane SET nume = ?, prenume = ?, data_nasterii = ?, ocupatie = ?, nivel_empatie = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Tranzactia
            connection.setAutoCommit(false);

            try (PreparedStatement psUser = connection.prepareStatement(updateUserSQL);
                 PreparedStatement psPersoana = connection.prepareStatement(updatePersoanaSQL)) {

                // 1. Update User
                psUser.setString(1, entity.getUsername());
                psUser.setString(2, entity.getEmail());
                psUser.setString(3, entity.getPassword());
                psUser.setLong(4, entity.getId());
                psUser.executeUpdate();

                // 2. Update Persoana
                psPersoana.setString(1, entity.getNume());
                psPersoana.setString(2, entity.getPrenume());
                if (entity.getDataNasterii() != null) {
                    psPersoana.setDate(3, java.sql.Date.valueOf(entity.getDataNasterii()));
                } else {
                    psPersoana.setNull(3, Types.DATE);
                }
                psPersoana.setString(4, entity.getOcupatie());
                psPersoana.setInt(5, entity.getNivelEmpatie());
                psPersoana.setLong(6, entity.getId());
                int response = psPersoana.executeUpdate();

                // Salvam ambele update-uri
                connection.commit();

                // Daca a reusit, return Optional.empty(). Daca nu a reusit, return entity
                return response > 0 ? Optional.empty(): Optional.of(entity);

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
