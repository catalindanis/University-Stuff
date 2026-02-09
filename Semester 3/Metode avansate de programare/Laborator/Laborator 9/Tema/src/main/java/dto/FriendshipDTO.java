package dto;

import models.FriendshipStatus;

public class FriendshipDTO {
    public Long user1;
    public Long user2;
    public FriendshipStatus status;
    public Long owner;

    public FriendshipDTO() {}

    public FriendshipDTO(Long user1, Long user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = FriendshipStatus.WAITING;
        this.owner = user1;
    }

    public FriendshipDTO(Long user1, Long user2, FriendshipStatus status) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.owner = user1;
    }

    public FriendshipDTO(Long user1, Long user2, FriendshipStatus status, Long owner) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.owner = owner;
    }
}
