package validation;

import domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship friendship) throws ValidationException {

        // --- 1. Validare ID ---
        if (friendship.getId() == null) {
            throw new ValidationException("Friendship ID cannot be null");
        }
        if (friendship.getId() <= 0) {
            throw new ValidationException("Friendship ID must be positive");
        }

        // --- 2. Validare user id ---
        if (friendship.getUser1Id() >= friendship.getUser2Id()) {
            throw new ValidationException("User1 ID must be lower than User2 ID");
        }

    }
}
