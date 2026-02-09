package dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class FriendshipsFilterDTO {
    private Optional<Long> user1;
    private Optional<Long> user2;

    public FriendshipsFilterDTO() {
        user1 = Optional.empty();
        user2 = Optional.empty();
    }
}
