package sm.control.boundary;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sm.entity.Subscription;

@ApplicationScoped
public class SubscriptionsRepository implements PanacheRepository<Subscription> {
}
