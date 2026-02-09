package repositories;

import models.Friendship;

import java.util.ArrayList;
import java.util.List;

public abstract class FriendshipsRepository implements Repository<Long, Friendship> {
    protected List<Friendship> friendships;

    public FriendshipsRepository() {
        friendships = new ArrayList<>();
    }

    @Override
    public abstract Friendship add(Friendship friendship);

    @Override
    public abstract Friendship remove(Friendship friendship);

    @Override
    public Friendship get(Long id) {
        return friendships.stream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Friendship> getAll() {
        return friendships;
    }
}
