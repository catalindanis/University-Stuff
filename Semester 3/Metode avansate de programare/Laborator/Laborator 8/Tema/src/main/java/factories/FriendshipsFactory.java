package factories;

import exceptions.FriendshipException;
import models.Friendship;
import models.FriendshipStatus;
import validators.FriendshipValidator;

public class FriendshipsFactory implements Factory<Friendship> {
    private static final FriendshipsFactory instance = new FriendshipsFactory();
    private final FriendshipValidator friendshipValidator = new FriendshipValidator();

    private FriendshipsFactory() {}

    public static FriendshipsFactory getInstance() {
        return instance;
    }

    public Friendship createFriendship(long id, Long user1, Long user2, FriendshipStatus status) {
        Friendship friendship = new Friendship(id, user1, user2, status);

        if(!friendshipValidator.validate(friendship))
            throw new FriendshipException("Invalid friendship");

        return friendship;
    }

    public Friendship createFriendship(long id, Long user1, Long user2, FriendshipStatus status, Long owner) {
        Friendship friendship = new Friendship(id, user1, user2, status, owner);

        if(!friendshipValidator.validate(friendship))
            throw new FriendshipException("Invalid friendship");

        return friendship;
    }
}
