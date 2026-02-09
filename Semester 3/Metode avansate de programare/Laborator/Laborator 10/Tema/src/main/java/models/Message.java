package models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class Message extends Entity<Long> {
    private Long fromUser;
    private List<Long> toUsers;
    private String message;
    private LocalDateTime dateTime;
    private Long reply;

    public Message(Long id, Long fromUser, List<Long> toUsers, String message, LocalDateTime dateTime) {
        super(id);
        this.fromUser = fromUser;
        this.toUsers = toUsers;
        this.message = message;
        this.dateTime = dateTime;
    }

    public Message(Long id, Long fromUser, List<Long> toUsers, String message, LocalDateTime dateTime, Long reply) {
        super(id);
        this.fromUser = fromUser;
        this.toUsers = toUsers;
        this.message = message;
        this.dateTime = dateTime;
        this.reply = reply;
    }
}
