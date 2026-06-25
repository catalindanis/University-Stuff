package sm.control.boundary;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sm.entity.User;

@ApplicationScoped
public class UsersRepository implements PanacheRepository<User> {
}
