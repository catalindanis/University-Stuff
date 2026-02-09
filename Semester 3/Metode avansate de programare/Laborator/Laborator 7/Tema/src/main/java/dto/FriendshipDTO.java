package dto;

public class FriendshipDTO {
    public Long user1;
    public Long user2;

    public FriendshipDTO() {}

    public FriendshipDTO(Long user1, Long user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
}
