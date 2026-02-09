package factories;

import lombok.Getter;
import models.Message;

import java.time.LocalDateTime;
import java.util.List;

public class MessageFactory implements Factory<Message> {
    @Getter
    private static final MessageFactory instance = new MessageFactory();

    private MessageFactory() {}

    public Message createMessage(
            long id,
            Long ownerId,
            List<Long> usersId,
            String message,
            LocalDateTime dateTime,
            Long reply
    ) {
        return new Message(
                id,
                ownerId,
                usersId,
                message,
                dateTime,
                reply
        );
    }
}
