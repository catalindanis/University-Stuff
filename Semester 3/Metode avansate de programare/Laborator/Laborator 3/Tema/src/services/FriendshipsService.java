package services;

import factories.FriendshipsFactory;
import models.Entity;
import models.Friendship;
import models.FriendshipDTO;
import repositories.FileFriendshipRepository;
import repositories.Repository;
import utils.GraphUtils;

import java.util.*;
import java.util.stream.Collectors;

public class FriendshipsService implements Service<Friendship> {
    private static FriendshipsService instance;
    private final Repository<Long, Friendship> repository;

    private FriendshipsService() {
        repository = new FileFriendshipRepository("friendships.csv");
    }

    public static FriendshipsService getInstance() {
        if (instance == null)
            instance = new FriendshipsService();

        return instance;
    }

    public Friendship add(FriendshipDTO friendshipDTO) {
        return repository.add(
                FriendshipsFactory.getInstance().createFriendship(
                        generateId(),
                        friendshipDTO.user1,
                        friendshipDTO.user2
                )
        );
    }

    public Friendship removeById(long id) {
        return repository.remove(repository.get(id));
    }

    public List<Friendship> removeFriendships(long id) {
        List<Friendship> removedFriendships = new ArrayList<>();

        new ArrayList<>(repository.getAll()).forEach(f -> {
            if(f.getUsers()[0] == id || f.getUsers()[1] == id) {
                removedFriendships.add(f);
                removeById(f.getId());
            }
        });

        return removedFriendships;
    }

    public Friendship getById(long id) {
        return repository.get(id);
    }

    public List<Friendship> getFriendships() {
        return repository.getAll();
    }

    public int getNumberOfCommunities() {
        Map<Long, Set<Long>> adjacencyList = GraphUtils.getAdjacencyList(getUsersIds(), repository.getAll());
        return GraphUtils.getNumberOfConnectedGraphs(adjacencyList);
    }

    public int getMostSociableCommunity() {
        Map<Long, Set<Long>> adjacencyList = GraphUtils.getAdjacencyList(getUsersIds(), repository.getAll());
        return GraphUtils.getLengthOfLongestConnectedGraph(adjacencyList);
    }

    private long generateId() {
        return repository.getAll().stream().map(Friendship::getId).max(Long::compare).orElse(0L) + 1;
    }

    public Set<Long> getUsersIds() {
        return UsersService.getInstance().getUsers().stream().map(Entity::getId).collect(Collectors.toSet());
    }
}
