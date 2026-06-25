package sm.control.boundary;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sm.entity.SubscriptionCategory;

@ApplicationScoped
public class SubscriptionsCategoryRepository implements PanacheRepository<SubscriptionCategory> {
}
