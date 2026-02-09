package repositories;

import dto.FriendshipsFilterDTO;
import exceptions.MessageException;
import lombok.RequiredArgsConstructor;
import models.Friendship;
import models.Message;
import services.FriendshipsService;
import utils.paging.Page;
import utils.paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
public class DatabaseMessageRepository implements DatabaseRepository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;

    List<Message> messages = new ArrayList<>();
    @Override
    public Message add(Message message) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO \"Messages\" (" +
                    "message, " +
                    "date_time, " +
                    "reply, " +
                    "owner_id" +
                    ") VALUES (?, ?, ?, ?) RETURNING id");

            statement.setString(1, message.getMessage());
            statement.setTimestamp(2, Timestamp.valueOf(message.getDateTime()));
            if (message.getReply() != null) {
                statement.setLong(3, message.getReply());
            } else {
                statement.setNull(3, java.sql.Types.BIGINT);
            }
            statement.setLong(4, message.getFromUser());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                long generatedId = rs.getLong(1);
//                System.out.println(generatedId);

                FriendshipsFilterDTO filterDTO = new FriendshipsFilterDTO();
                for(var friendId : message.getToUsers()) {
//                    System.out.println(friendId);
                    filterDTO.setUser1(Optional.of(
                            Math.min(message.getFromUser(), friendId)
                    ));

                    filterDTO.setUser2(Optional.of(
                            Math.max(message.getFromUser(), friendId)
                    ));

                    Page<Friendship> page = FriendshipsService.getInstance().getFriendships(new Pageable(0, 1), filterDTO);

                    statement = connection.prepareStatement("INSERT INTO \"FriendshipsMessage\" (" +
                            "friendship_id, " +
                            "message_id" +
                            ") VALUES (?, ?)");

                    statement.setLong(1, page.getElements().getFirst().getId());
                    statement.setLong(2, generatedId);

                    statement.executeUpdate();
                }
            }

            return message;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message remove(Message message) { return null; }

    @Override
    public Message get(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM \"Messages\" WHERE id = ?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                String message = resultSet.getString("message");
                Long ownerId = resultSet.getLong("owner_id");
                Long reply = resultSet.getLong("reply");
                if(resultSet.wasNull())
                    reply = null;
                LocalDateTime dateTime = resultSet.getTimestamp("date_time").toLocalDateTime();

                statement = connection.prepareStatement("SELECT * FROM \"FriendshipsMessage\" WHERE message_id = ?");
                statement.setLong(1, id);

                Set<Long> friends = new HashSet<>();
                resultSet = statement.executeQuery();

                while(resultSet.next()) {
                    Long friendshipId = resultSet.getLong("friendship_id");
                    statement = connection.prepareStatement("SELECT * FROM \"Friendships\" WHERE id = ?");
                    statement.setLong(1, friendshipId);

                    ResultSet resultSet1 = statement.executeQuery();

                    if(resultSet1.next()) {
                        friends.add(resultSet1.getLong("user1"));
                        friends.add(resultSet1.getLong("user2"));
                    }
                }
                friends.remove(ownerId);

                return new Message(
                        id,
                        ownerId,
                        friends.stream().toList(),
                        message,
                        dateTime,
                        reply
                );
            }

            throw new MessageException("Mesaj negasit");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Message> getAll() { return null; }

    public List<Message> getAllByFriendship(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            List<Message> messages = new ArrayList<>();

            var statement = connection.prepareStatement("SELECT * FROM \"FriendshipsMessage\" WHERE friendship_id = ?");
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long message_id = resultSet.getLong("message_id");

                messages.add(this.get(message_id));
            }

            return messages;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message update(Long id, Message message) { return null; }
}
