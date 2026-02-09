package factories;

import exceptions.FriendshipException;
import models.Friendship;
import validators.FriendshipValidator;

public class FriendshipsFactory implements Factory<Friendship> {
    private static final FriendshipsFactory instance = new FriendshipsFactory();
    private final FriendshipValidator friendshipValidator = new FriendshipValidator();

    private FriendshipsFactory() {}

    public static FriendshipsFactory getInstance() {
        return instance;
    }

    public Friendship createFriendship(long id, Long user1, Long user2) {
        Friendship friendship = new Friendship(id, user1, user2);

        if(!friendshipValidator.validate(friendship))
            throw new FriendshipException("Invalid friendship");

        return friendship;
    }
}
