package validators;

import models.Friendship;
import services.FriendshipsService;
import services.UsersService;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public boolean validate(Friendship friendship) {
        if(friendship.getUsers()[0].equals(friendship.getUsers()[1]))
            return false;

        if(UsersService.getInstance().getById(friendship.getUsers()[0]) == null ||
        UsersService.getInstance().getById(friendship.getUsers()[1]) == null)
            return false;

//        return !FriendshipsService.getInstance().getFriendships().stream().anyMatch(f -> f.equals(friendship));
        return true;
    }
}
