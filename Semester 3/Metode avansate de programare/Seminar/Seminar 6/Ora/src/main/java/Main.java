import models.User;
import repos.Repository;
import repos.UserRepository;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/s6map";
        String username = "postgres";
        String password = "dbcata05";

        Repository<Integer, User> repo = new UserRepository<>(url, username, password);

        repo.findById(1);
        repo.findById(2);

        var user = new User("ana", LocalDateTime.now(), LocalDateTime.now(), 2);
        repo.save(user);

    }
}
