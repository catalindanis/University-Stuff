package services;

import config.Config;
import dto.FriendshipDTO;
import dto.FriendshipsFilterDTO;
import factories.FriendshipsFactory;
import models.Entity;
import models.Friendship;
import org.example.tema.FriendshipsController;
import repositories.DatabaseFriendshipsRepository;
import repositories.PageableRepository;
import utils.GraphUtils;
import utils.Observable;
import utils.Observer;
import utils.paging.Page;
import utils.paging.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FriendshipsService implements Service<Friendship>, Observable<FriendshipsController> {
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
    public void subscribe(FriendshipsController friendshipsController) {
        observers.add(friendshipsController);
    }

    @Override
    public void unsubscribe(FriendshipsController friendshipsController) {
        observers.remove(friendshipsController);
    }

    @Override
    public void notifyObservers() {
        for(var observer: observers)
            observer.update(this);
    }
}
