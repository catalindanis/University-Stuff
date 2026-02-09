package Service;
import Observer.Observable;
import Repository.*;
import java.util.List;

public class Service implements Observable {
    private ItemRepo repo;
    private ItemRepoPaged repoPaged;
    private ItemRepoAsync repoAsync;

    public Service(ItemRepo r, ItemRepoPaged rp, ItemRepoAsync ra) {
        this.repo = r; this.repoPaged = rp; this.repoAsync = ra;
    }

    // Aici schimbi rapid intre repo-uri
    public List<Object> getData() { return repo.findAll(); }

    public void executeAsyncAction() {
        repoAsync.findAllAsync().thenAccept(data -> {
            // Procesare asincrona
            notifyObservers();
        });
    }

    public void addData() {
        // repo.save(...);
        notifyObservers();
    }
}