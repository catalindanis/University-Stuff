package dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MessagesFilterDTO {
    private Optional<Long> fromUser;
    private Optional<List<Long>> toUsers;
    private Optional<String> message;
    private Optional<LocalDateTime> dateTime;
    private Optional<Long> reply;
    private Optional<Long> friendshipId;

    public MessagesFilterDTO() {
        fromUser = Optional.empty();
        toUsers = Optional.empty();
        message = Optional.empty();
        dateTime = Optional.empty();
        reply = Optional.empty();
        friendshipId = Optional.empty();
    }
}
