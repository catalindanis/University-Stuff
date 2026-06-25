package sm.control.boundary;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import sm.entity.PaymentAlert;

@ApplicationScoped
public class PaymentAlertsRepository implements PanacheRepository<PaymentAlert> {
}
