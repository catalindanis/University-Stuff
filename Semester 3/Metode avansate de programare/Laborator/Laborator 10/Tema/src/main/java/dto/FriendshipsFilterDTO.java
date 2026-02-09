package dto;

import lombok.Getter;
import lombok.Setter;
import models.FriendshipStatus;

import java.util.Optional;

@Getter
@Setter
public class FriendshipsFilterDTO {
    private Optional<Long> user1;
    private Optional<Long> user2;
    private Optional<FriendshipStatus> status;

    public FriendshipsFilterDTO() {
        user1 = Optional.empty();
        user2 = Optional.empty();
        status = Optional.empty();
    }
}
