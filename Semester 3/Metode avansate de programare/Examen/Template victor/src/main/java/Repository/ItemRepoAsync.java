package Repository;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ItemRepoAsync extends ItemRepo {
    public ItemRepoAsync(String url, String u, String p) { super(url, u, p); }

    public CompletableFuture<List<Object>> findAllAsync() {
        return CompletableFuture.supplyAsync(this::findAll);
    }
}