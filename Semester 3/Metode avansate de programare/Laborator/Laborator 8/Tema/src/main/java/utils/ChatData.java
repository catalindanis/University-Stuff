package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatData {
    private Long ownerId;
    private List<Long> usersId;
    private Long friendshipId;
}
