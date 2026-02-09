package repository;

import domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class EventDBRepository implements Repository<Long, Event> {

    private String url;
    private String username;
    private String password;

    public EventDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Event> findOne(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM events WHERE id = ?")) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            // 1. Citim datele unui Event
            if (resultSet.next()) {
                String descriere = resultSet.getString("descriere");
                String tipEvent = resultSet.getString("tip_event").toLowerCase();

                Event event;

                switch (tipEvent) {
                    case "race" -> {
                        // 2. Citim datele specifice unui RaceEvent
                        try (PreparedStatement statement2 = connection.prepareStatement("SELECT distanta_balize FROM race_events WHERE id = ?")) {

                            statement2.setLong(1, id);
                            ResultSet resultSet2 = statement2.executeQuery();

                            List<Double> distantaBalize = new ArrayList<>();

                            if (resultSet2.next()) {
                                Array array = resultSet2.getArray("distanta_balize");
                                if (array != null) {
                                    Double[] values = (Double[]) array.getArray();
                                    distantaBalize = Arrays.asList(values);
                                }
                            }

                            event = new RaceEvent(descriere, distantaBalize);
                        }
                    }
                    default -> throw new RuntimeException("Unknown event type: " + tipEvent);
                }

                event.setId(id);

                // Daca a reusit, return eventul
                return Optional.of(event);
            }

            // Daca nu a reusit, return Optional.empty()
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Iterable<Event> findAll() {

        Set<Event> events = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM events")) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Long id = resultSet.getLong("id");
                String descriere = resultSet.getString("descriere");
                String tipEvent = resultSet.getString("tip_event").toLowerCase();

                Event event;

                switch (tipEvent) {
                    case "race" -> {
                        try (PreparedStatement statement2 = connection.prepareStatement("SELECT distanta_balize FROM race_events WHERE id = ?")) {

                            statement2.setLong(1, id);
                            ResultSet resultSet2 = statement2.executeQuery();

                            List<Double> distantaBalize = new ArrayList<>();

                            if (resultSet2.next()) {
                                Array array = resultSet2.getArray("distanta_balize");
                                if (array != null) {
                                    Double[] values = (Double[]) array.getArray();
                                    distantaBalize = Arrays.asList(values);
                                }
                            }

                            event = new RaceEvent(descriere, distantaBalize);
                        }
                    }
                    default -> throw new RuntimeException("Unknown event type: " + tipEvent);
                }

                event.setId(id);
                events.add(event);
            }

            return events;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Optional<Event> save(Event entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Tranzactie, pentru a fi adaugat complet
            connection.setAutoCommit(false);

            // 1. Inseram prima data in events
            String insertEventSQL = "INSERT INTO events (descriere, tip_event) VALUES (?, ?)";
            try (PreparedStatement psEvent = connection.prepareStatement(insertEventSQL, Statement.RETURN_GENERATED_KEYS)) {

                psEvent.setString(1, entity.getDescriere());
                psEvent.setString(2, entity.getTip());
                int eventInserted = psEvent.executeUpdate();

                // Daca nu a reusit save, return entity
                if (eventInserted == 0) {
                    connection.rollback();
                    return Optional.of(entity);
                }

                // Obtinem id-ul generat de DB
                ResultSet generatedKeys = psEvent.getGeneratedKeys();
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    entity.setId(id);

                    // 2. Daca e RaceEvent, inseram in tabela race_events
                    if (entity instanceof RaceEvent raceEvent) {
                        List<Double> distantaBalize = raceEvent.getDistantaBalize();
                        Double[] array = distantaBalize.toArray(new Double[0]);
                        Array sqlArray = connection.createArrayOf("DOUBLE", array);

                        String insertRaceSQL = "INSERT INTO race_events (id, distanta_balize) VALUES (?, ?)";
                        try (PreparedStatement psRace = connection.prepareStatement(insertRaceSQL)) {
                            psRace.setLong(1, id);
                            psRace.setArray(2, sqlArray);

                            // Daca nu a reusit save, return entity
                            int raceInserted = psRace.executeUpdate();
                            if (raceInserted == 0) {
                                connection.rollback();
                                return Optional.of(entity);
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Unknown event type: " + entity.getClass());
                    }

                    // Daca a reusit, commit si return Optional.empty()
                    connection.commit();
                    return Optional.empty();

                // Daca nu s-a generat id, return entity
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
    public Optional<Event> delete(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        // 1. Verificam daca exista eventul inainte sa il stergem
        Optional<Event> existingEvent = findOne(id);
        if (existingEvent.isEmpty()) {
            return Optional.empty();
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Tranzactie
            connection.setAutoCommit(false);

            // 2. Stergem din tabelele specifice
            String deleteRaceSQL = "DELETE FROM race_events WHERE id = ?";
            try (PreparedStatement psRace = connection.prepareStatement(deleteRaceSQL)) {
                psRace.setLong(1, id);

                // Daca nu exista, nu conteaza
                psRace.executeUpdate();
            }

            // 3. Stergem din tabela events
            String deleteEventSQL = "DELETE FROM events WHERE id = ?";
            try (PreparedStatement psEvent = connection.prepareStatement(deleteEventSQL)) {
                psEvent.setLong(1, id);

                // Daca nu exista event, returnam Optional.empty()
                int rowsDeleted = psEvent.executeUpdate();
                if (rowsDeleted == 0) {
                    connection.rollback();
                    return Optional.empty();
                }
            }

            // Daca a reusit, commit si return eventul sters
            connection.commit();
            return existingEvent;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Event> update(Event entity) {

        if (entity == null) {
            throw new IllegalArgumentException("Entity cannot be null");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Tranzactia
            connection.setAutoCommit(false);

            // 1. Update in tabela events
            String updateEventSQL = "UPDATE events SET descriere = ?, tip_event = ? WHERE id = ?";
            try (PreparedStatement psEvent = connection.prepareStatement(updateEventSQL)) {
                psEvent.setString(1, entity.getDescriere());
                psEvent.setString(2, entity.getTip());
                psEvent.setLong(3, entity.getId());

                // Daca nu a reusit, return entity
                int eventInserted = psEvent.executeUpdate();
                if (eventInserted == 0) {
                    connection.rollback();
                    return Optional.of(entity);
                }

                // 2. Update in tabelele specifice
                if (entity instanceof RaceEvent raceEvent) {
                    List<Double> distantaBalize = raceEvent.getDistantaBalize();
                    Double[] array = distantaBalize.toArray(new Double[0]);
                    Array sqlArray = connection.createArrayOf("DOUBLE", array);

                    String updateRaceSQL = "UPDATE race_events SET distanta_balize = ? WHERE id = ?";
                    try (PreparedStatement psRace = connection.prepareStatement(updateRaceSQL)) {
                        psRace.setArray(1, sqlArray);
                        psRace.setLong(2, entity.getId());

                        // Daca nu exista in race_events, return entity
                        int raceUpdated = psRace.executeUpdate();
                        if (raceUpdated == 0) {
                            connection.rollback();
                            return Optional.of(entity);
                        }
                    }

                } else {
                    throw new IllegalArgumentException("Unknown event type: " + entity.getClass());
                }

                connection.commit();

                return Optional.empty();

            // Daca ceva nu merge, revenim la starea initiala
            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void addSubscription(Long eventId, Long userId) {

        if (eventId == null) {
            throw new IllegalArgumentException("Event Id cannot be null");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User Id cannot be null");
        }

        try (Connection c = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = c.prepareStatement("INSERT INTO event_subscribers(event_id, user_id) VALUES (?, ?)")) {

            ps.setLong(1, eventId);
            ps.setLong(2, userId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void removeSubscription(Long eventId, Long userId) {

        if (eventId == null) {
            throw new IllegalArgumentException("Event Id cannot be null");
        }

        if (userId == null) {
            throw new IllegalArgumentException("User Id cannot be null");
        }

        try (Connection c = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = c.prepareStatement("DELETE FROM event_subscribers WHERE event_id = ? AND user_id = ?")) {

            ps.setLong(1, eventId);
            ps.setLong(2, userId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Iterable<User> getSubscribers(Long eventId) {

        List<User> subscribers = new ArrayList<>();

        String sql = "SELECT u.id, u.username, u.email, u.password FROM event_subscribers es " +
                "INNER JOIN users u ON es.user_id = u.id WHERE es.event_id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, eventId);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Long userId = resultSet.getLong("id");
                String username2 = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password2 = resultSet.getString("password");

                // Verificam daca e Duck
                String checkDuckSQL = "SELECT viteza, rezistenta, card_id, duck_type FROM ducks WHERE id = ?";
                try (PreparedStatement psDuck = connection.prepareStatement(checkDuckSQL)) {
                    psDuck.setLong(1, userId);
                    ResultSet resultSetDuck = psDuck.executeQuery();

                    if (resultSetDuck.next()) {
                        double viteza = resultSetDuck.getDouble("viteza");
                        double rezistenta = resultSetDuck.getDouble("rezistenta");
                        Long cardId = resultSetDuck.getLong("card_id");
                        String duckType = resultSetDuck.getString("duck_type");

                        Duck duck;
                        switch (duckType) {
                            case "swimming" -> duck = new SwimmingDuck(username2, email, password2, viteza, rezistenta, cardId);
                            case "flying" -> duck = new FlyingDuck(username2, email, password2, viteza, rezistenta, cardId);
                            case "hybrid" -> duck = new HybridDuck(username2, email, password2, viteza, rezistenta, cardId);
                            default -> throw new RuntimeException("Unknown duck type: " + duckType);
                        }

                        // Adaugam la abonati si mergem la urmatorul user
                        duck.setId(userId);
                        subscribers.add(duck);
                        continue;
                    }
                }

                // Daca nu e Duck, verificam daca e Persoana
                String checkPersoanaSQL = "SELECT nume, prenume, data_nasterii, ocupatie, nivel_empatie FROM persoane WHERE id = ?";
                try (PreparedStatement psPers = connection.prepareStatement(checkPersoanaSQL)) {
                    psPers.setLong(1, userId);
                    ResultSet resultSetPers = psPers.executeQuery();

                    if (resultSetPers.next()) {
                        String nume = resultSetPers.getString("nume");
                        String prenume = resultSetPers.getString("prenume");
                        LocalDate dataNasterii = resultSetPers.getDate("data_nasterii").toLocalDate();
                        String ocupatie = resultSetPers.getString("ocupatie");
                        int nivelEmpatie = resultSetPers.getInt("nivel_empatie");

                        Persoana persoana = new Persoana(username2, email, password2, nume, prenume, dataNasterii, ocupatie, nivelEmpatie);
                        persoana.setId(userId);
                        subscribers.add(persoana);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return subscribers;

    }

}
