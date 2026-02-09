package repositories;

import dto.GroupDTO;
import exceptions.EventException;
import exceptions.RepositoryException;
import models.*;
import services.GroupsService;
import services.UsersService;
import validators.EventValidator;
import validators.RaceEventValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public synchronized Event add(String name, Event event) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement insertEvent = connection.prepareStatement(
                    "INSERT INTO \"Events\" (type) VALUES (?) RETURNING id"
            );
            if (event instanceof RaceEvent)
                insertEvent.setString(1, "RACE");

            ResultSet rs = insertEvent.executeQuery();
            if (rs.next()) event.setId(rs.getLong(1));

            if (event instanceof RaceEvent) {
                RaceEvent re = (RaceEvent) event;

                GroupDTO groupDTO = new GroupDTO();
                groupDTO.ducksIds = new ArrayList<>();
                groupDTO.name = name;
                groupDTO.ducksType = DuckType.SWIMMING;
                Group createdGroup = GroupsService.getInstance().add(groupDTO);

                PreparedStatement insertRace = connection.prepareStatement(
                        "INSERT INTO \"RaceEvents\" (event_id, group_id) VALUES (?, ?)"
                );
                insertRace.setLong(1, event.getId());
                insertRace.setLong(2, createdGroup.getId());
                insertRace.executeUpdate();

                PreparedStatement insertLane = connection.prepareStatement(
                        "INSERT INTO \"RaceEventLanes\" (event_id, buoy_distance) VALUES (?, ?) RETURNING lane_no"
                );

                for (Lane lane : re.getRace().getLanes()) {
                    insertLane.setLong(1, event.getId());
                    insertLane.setInt(2, lane.getBuoyDistance());
                    ResultSet laneRs = insertLane.executeQuery();
                    if (laneRs.next()) {
                        lane.setNumber(laneRs.getInt(1));
                    }
                }
            }

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
    public synchronized Event add(Event event) {
        if (event instanceof RaceEvent) {
            if (!raceEventValidator.validate((RaceEvent) event)) {
                throw new EventException("Invalid RaceEvent");
            }
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement insertEvent = connection.prepareStatement(
                    "INSERT INTO \"Events\" (type) VALUES (?) RETURNING id"
            );
            if (event instanceof RaceEvent)
                insertEvent.setString(1, "RACE");

            ResultSet rs = insertEvent.executeQuery();
            if (rs.next()) event.setId(rs.getLong(1));

            if (event instanceof RaceEvent) {
                RaceEvent re = (RaceEvent) event;

                GroupDTO groupDTO = new GroupDTO();
                groupDTO.name = "Event #" + event.getId();
                groupDTO.ducksType = DuckType.SWIMMING;
                Group createdGroup = GroupsService.getInstance().add(groupDTO);

                PreparedStatement insertRace = connection.prepareStatement(
                        "INSERT INTO \"RaceEvents\" (event_id, group_id) VALUES (?, ?)"
                );
                insertRace.setLong(1, event.getId());
                insertRace.setLong(2, createdGroup.getId());
                insertRace.executeUpdate();

                PreparedStatement insertLane = connection.prepareStatement(
                        "INSERT INTO \"RaceEventLanes\" (event_id, buoy_distance) VALUES (?, ?) RETURNING lane_no"
                );

                for (Lane lane : re.getRace().getLanes()) {
                    insertLane.setLong(1, event.getId());
                    insertLane.setInt(2, lane.getBuoyDistance());
                    ResultSet laneRs = insertLane.executeQuery();
                    if (laneRs.next()) {
                        lane.setNumber(laneRs.getInt(1));
                    }
                }
            }

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
    public synchronized Event remove(Event event) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM \"RaceEvents\" WHERE event_id = ?"
            );
            st.setLong(1, event.getId());
            st.executeUpdate();

            st = connection.prepareStatement(
                    "DELETE FROM \"RaceEventLanes\" WHERE event_id = ?"
            );
            st.setLong(1, event.getId());
            st.executeUpdate();

            st = connection.prepareStatement(
                    "DELETE FROM \"EventSubscribers\" WHERE event_id = ?"
            );
            st.setLong(1, event.getId());
            st.executeUpdate();

            if(event instanceof RaceEvent) {
                st = connection.prepareStatement(
                        "DELETE FROM \"Groups\" WHERE id = ?"
                );
                st.setLong(1, ((RaceEvent) event).getGroupId());
                st.executeUpdate();
            }

            st = connection.prepareStatement(
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

                List<Duck> ducks = UsersService.getInstance().getDucks().stream().filter(duck -> duck.getGroup() == group.getId()).collect(Collectors.toList());
                RaceEvent raceEvent = new RaceEvent(id, group.getId(), ducks, lanes);

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
    public synchronized Event update(Long id, Event event) {
        remove(get(id));
        return add(event);
    }

    public synchronized void subscribe(long eventId, long userId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Subscribe");
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

    public synchronized void unsubscribe(long eventId, long userId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Unsubscribe");
            PreparedStatement st = connection.prepareStatement(
                    "DELETE FROM \"EventSubscribers\" WHERE event_id = ? AND user_id = ?"
            );
            st.setLong(1, eventId);
            st.setLong(2, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public synchronized void join(long eventId, long userId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            long groupId = getEventGroupId(connection, eventId);
            Duck duck = (Duck) UsersService.getInstance().getById(userId);
            duck.setGroup(groupId);
            UsersService.getInstance().update(userId, duck);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public synchronized void unjoin(long eventId, long userId) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Duck duck = (Duck) UsersService.getInstance().getById(userId);
            duck.setGroup(-1);
            UsersService.getInstance().update(userId, duck);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    private long getEventGroupId(Connection connection, long eventId) throws SQLException {
        PreparedStatement st = connection.prepareStatement(
                "SELECT * FROM \"RaceEvents\" WHERE event_id = ?"
        );
        st.setLong(1, eventId);
        ResultSet resultSet = st.executeQuery();

        if(!resultSet.next()) throw new EventException("Race event missing");

        return resultSet.getLong("group_id");
    }
}
