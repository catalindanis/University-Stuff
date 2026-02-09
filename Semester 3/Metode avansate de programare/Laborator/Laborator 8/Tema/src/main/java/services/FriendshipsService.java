package services;

import config.Config;
import dto.FriendshipDTO;
import dto.FriendshipsFilterDTO;
import factories.FriendshipsFactory;
import models.Entity;
import models.Friendship;
import models.FriendshipStatus;
import models.User;
import repositories.DatabaseFriendshipsRepository;
import repositories.PageableRepository;
import utils.GraphUtils;
import utils.Observable;
import utils.Observer;
import utils.paging.Page;
import utils.paging.Pageable;

import java.util.*;
import java.util.stream.Collectors;

public class FriendshipsService implements Service<Friendship>, Observable<Observer<FriendshipsService>> {
    private static FriendshipsService instance;
    private final PageableRepository<Long, Friendship, FriendshipsFilterDTO> repository;
    private final List<Observer<FriendshipsService>> observers;

    private FriendshipsService() {
        String url = Config.getProperties().getProperty("db.url");
        String username = Config.getProperties().getProperty("db.username");
        String password = Config.getProperties().getProperty("db.password");
        repository = new DatabaseFriendshipsRepository(url, username, password);

        observers = new ArrayList<>();
    }

    public static FriendshipsService getInstance() {
        if (instance == null) {
            instance = new FriendshipsService();
        }

        return instance;
    }

    public Friendship add(FriendshipDTO friendshipDTO) {
        Friendship add = repository.add(
                friendshipDTO.owner == null ?
                FriendshipsFactory.getInstance().createFriendship(
                        generateId(),
                        friendshipDTO.user1,
                        friendshipDTO.user2,
                        friendshipDTO.status
                ) :
                        FriendshipsFactory.getInstance().createFriendship(
                                generateId(),
                                friendshipDTO.user1,
                                friendshipDTO.user2,
                                friendshipDTO.status,
                                friendshipDTO.owner
                        )
        );

        notifyObservers();
        return add;
    }

    public Friendship update(long id, FriendshipDTO friendshipDTO) {
        Friendship update = repository.update(
                id,
                friendshipDTO.owner == null ?
                        FriendshipsFactory.getInstance().createFriendship(
                        generateId(),
                        friendshipDTO.user1,
                        friendshipDTO.user2,
                        friendshipDTO.status
                ) :
                    FriendshipsFactory.getInstance().createFriendship(
                            generateId(),
                            friendshipDTO.user1,
                            friendshipDTO.user2,
                            friendshipDTO.status,
                            friendshipDTO.owner
                    )
        );

        notifyObservers();
        return update;
    }

    public Friendship removeById(long id) {
        Friendship remove = repository.remove(repository.get(id));

        notifyObservers();
        return remove;
    }

    public Friendship removeByUsers(long user1, long user2) {
        Friendship friendship = getFriendship(user1, user2);

        if(friendship == null)
            removeById(-1);

        Friendship remove = removeById(friendship.getId());
        notifyObservers();
        return remove;
    }

    public Friendship acceptByUsers(long user1, long user2) {
        Friendship friendship = getFriendship(user1, user2);

        if(friendship == null)
            removeById(-1);

        friendship.setStatus(FriendshipStatus.APPROVED);
        repository.update(friendship.getId(), friendship);
        notifyObservers();
        return friendship;
    }

    private Friendship getFriendship(long user1, long user2) {
        Friendship friendship = getFriendships().stream()
                .filter(f -> f.getUsers()[0] == Math.min(user1, user2) && f.getUsers()[1] == Math.max(user1, user2) && f.getStatus() == FriendshipStatus.WAITING)
                .findFirst().orElse(null);
        return friendship;
    }

    public Friendship rejectByUsers(long user1, long user2) {
        Friendship friendship = getFriendship(user1, user2);

        if(friendship == null)
            removeById(-1);

        friendship.setStatus(FriendshipStatus.REFUSED);
        repository.update(friendship.getId(), friendship);
        notifyObservers();
        return friendship;
    }

    public List<Friendship> removeFriendships(long id) {
        List<Friendship> removedFriendships = new ArrayList<>();

        new ArrayList<>(repository.getAll()).forEach(f -> {
            if(f.getUsers()[0] == id || f.getUsers()[1] == id) {
                removedFriendships.add(f);
                removeById(f.getId());
            }
        });

        notifyObservers();
        return removedFriendships;
    }

    public Friendship getById(long id) {
        return repository.get(id);
    }

    public List<Friendship> getFriendships() {
        return repository.getAll();
    }

    public Page<Friendship> getFriendships(Pageable pageable, FriendshipsFilterDTO filterDTO) {
        return repository.getAllOnPage(pageable, filterDTO);
    }

    public List<User> getFriendsForUser(long id) {
        Set<User> friends = new HashSet<>();

        repository.getAll().forEach(f -> {
            if(f.getUsers()[0] == id && f.getStatus() == FriendshipStatus.APPROVED)
                friends.add(UsersService.getInstance().getById(f.getUsers()[1]));
            if(f.getUsers()[1] == id && f.getStatus() == FriendshipStatus.APPROVED)
                friends.add(UsersService.getInstance().getById(f.getUsers()[0]));
        });

        return friends.stream().toList();
    }

    public List<User> getMyPendingFriendsForUser(long id) {
        Set<User> friends = new HashSet<>();

        repository.getAll().forEach(f -> {
            if (f.getUsers()[0] == id && f.getStatus() == FriendshipStatus.WAITING && f.getOwner() == id)
                friends.add(UsersService.getInstance().getById(f.getUsers()[1]));
            if (f.getUsers()[1] == id && f.getStatus() == FriendshipStatus.WAITING && f.getOwner() == id)
                friends.add(UsersService.getInstance().getById(f.getUsers()[0]));
        });

        return friends.stream().toList();
    }

    public List<User> getOthersPendingFriendsForUser(long id) {
        Set<User> friends = new HashSet<>();

        repository.getAll().forEach(f -> {
            if (f.getUsers()[0] == id && f.getStatus() == FriendshipStatus.WAITING && f.getOwner() != id)
                friends.add(UsersService.getInstance().getById(f.getUsers()[1]));
            if (f.getUsers()[1] == id && f.getStatus() == FriendshipStatus.WAITING && f.getOwner() != id)
                friends.add(UsersService.getInstance().getById(f.getUsers()[0]));
        });

        return friends.stream().toList();
    }

    public List<User> getNonFriendsForUser(long id) {
        List<User> friends = getFriendsForUser(id);
        List<User> myPendingFriends = getMyPendingFriendsForUser(id);
        List<User> otherPendingFriends = getOthersPendingFriendsForUser(id);

        return UsersService.getInstance().getUsers().stream()
                .filter(f ->
                        !friends.contains(f) &&
                        !myPendingFriends.contains(f) &&
                        !otherPendingFriends.contains(f) &&
                        f.getId() != id
                ).collect(Collectors.toList());
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

    @Override
    public void subscribe(Observer<FriendshipsService> friendshipsServiceObserver) {
        observers.add(friendshipsServiceObserver);
    }

    @Override
    public void unsubscribe(Observer<FriendshipsService> friendshipsServiceObserver) {
        observers.remove(friendshipsServiceObserver);
    }

    @Override
    public void notifyObservers() {
        for(var observer: observers)
            observer.update(this);
    }
}
