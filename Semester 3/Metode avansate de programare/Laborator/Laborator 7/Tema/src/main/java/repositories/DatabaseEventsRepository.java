package repositories;

import exceptions.RepositoryException;
import exceptions.EventException;
import models.*;
import services.UsersService;
import services.GroupsService;
import validators.EventValidator;
import validators.RaceEventValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseEventsRepository implements DatabaseRepository<Long, Event> {
    private final String url;
    private final String username;
    private final String password;
    private final EventValidator raceEventValidator = new RaceEventValidator();

    public DatabaseEventsRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Event add(Event event) {
        if (event instanceof RaceEvent) {
            if (!raceEventValidator.validate((RaceEvent) event)) {
                throw new EventException("Invalid RaceEvent");
            }
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Insert event
            PreparedStatement insertEvent = connection.prepareStatement(
                    "INSERT INTO \"Events\" (type) VALUES (?) RETURNING id"
            );
            if (event instanceof RaceEvent)
                insertEvent.setString(1, "RACE");

            ResultSet rs = insertEvent.executeQuery();
            if (rs.next()) event.setId(rs.getLong(1));

            if (event instanceof RaceEvent) {
                RaceEvent re = (RaceEvent) event;

                // Insert RaceEvent table
                PreparedStatement insertRace = connection.prepareStatement(
                        "INSERT INTO \"RaceEvents\" (event_id, group_id) VALUES (?, ?)"
                );
                insertRace.setLong(1, event.getId());
                insertRace.setLong(2, re.getRace().getDucks().get(0).getGroup());
                insertRace.executeUpdate();

                // Insert lanes without lane_no (auto-generated)
                PreparedStatement insertLane = connection.prepareStatement(
                        "INSERT INTO \"RaceEventLanes\" (event_id, buoy_distance) VALUES (?, ?) RETURNING lane_no"
                );

                for (Lane lane : re.getRace().getLanes()) {
                    insertLane.setLong(1, event.getId());
                    insertLane.setInt(2, lane.getBuoyDistance());
                    ResultSet laneRs = insertLane.executeQuery();
                    if (laneRs.next()) {
                        lane.setNumber(laneRs.getInt(1)); // update Lane with DB-generated number
                    }
                }
            }

            // Insert subscribers
            PreparedStatement insertSub = connection.prepareStatement(
                    "INSERT INTO \"EventSubscribers\" (event_id, user_id) VALUES (?, ?)"
            );
            for (User u : event.getSubscribers()) {
                insertSub.setLong(1, event.getId());
                insertSub.setLong(2, u.getId());
                insertSub.executeUpdate();
            }

            return event;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Event remove(Event event) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM \"Events\" WHERE id = ?"
            );
            st.setLong(1, event.getId());
            int r = st.executeUpdate();
            return r != 0 ? event : null;
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Event get(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement getEvent = connection.prepareStatement(
                    "SELECT * FROM \"Events\" WHERE id = ?"
            );
            getEvent.setLong(1, id);
            ResultSet rs = getEvent.executeQuery();
            if (!rs.next()) throw new EventException("Eveniment negasit");

            String type = rs.getString("type");

            if (type.equals("RACE")) {
                PreparedStatement getRace = connection.prepareStatement(
                        "SELECT * FROM \"RaceEvents\" WHERE event_id = ?"
                );
                getRace.setLong(1, id);
                ResultSet rsRace = getRace.executeQuery();
                if (!rsRace.next()) throw new EventException("Race event missing");

                long groupId = rsRace.getLong("group_id");
                SwimmingGroup group = (SwimmingGroup) GroupsService.getInstance().getById(groupId);

                PreparedStatement getLanes = connection.prepareStatement(
                        "SELECT * FROM \"RaceEventLanes\" WHERE event_id = ? ORDER BY lane_no"
                );
                getLanes.setLong(1, id);
                ResultSet rsLanes = getLanes.executeQuery();
                List<Lane> lanes = new ArrayList<>();
                while (rsLanes.next()) {
                    int laneNo = rsLanes.getInt("lane_no");
                    int buoyDistance = rsLanes.getInt("buoy_distance");
                    lanes.add(new Lane(laneNo, buoyDistance));
                }

                RaceEvent raceEvent = new RaceEvent(id, group, lanes);

                PreparedStatement getSubs = connection.prepareStatement(
                        "SELECT user_id FROM \"EventSubscribers\" WHERE event_id = ?"
                );
                getSubs.setLong(1, id);
                ResultSet rsSubs = getSubs.executeQuery();
                while (rsSubs.next()) {
                    long uid = rsSubs.getLong("user_id");
                    raceEvent.subscribe(UsersService.getInstance().getById(uid));
                }

                return raceEvent;
            }

            throw new EventException("Unknown event type");
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement st = connection.prepareStatement(
                    "SELECT id FROM \"Events\""
            );
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                events.add(get(rs.getLong("id")));
            }
            return events;
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Event update(Long id, Event event) {
        remove(get(id));
        return add(event);
    }

    public void subscribe(long eventId, long userId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement st = connection.prepareStatement(
                    "INSERT INTO \"EventSubscribers\" (event_id, user_id) VALUES (?, ?)"
            );
            st.setLong(1, eventId);
            st.setLong(2, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
