package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class MessageDTO {
    private Long ownerId;
    private List<Long> usersId;
    private String message;
    private LocalDateTime dateTime;

    @Setter
    private Long reply;
}
