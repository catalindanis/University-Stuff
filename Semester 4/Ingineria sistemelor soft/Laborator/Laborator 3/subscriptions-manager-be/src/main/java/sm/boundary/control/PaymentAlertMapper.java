package sm.boundary.control;

import jakarta.enterprise.context.ApplicationScoped;
import sm.boundary.entity.dto.PaymentAlertResponse;
import sm.entity.PaymentAlert;

@ApplicationScoped
public class PaymentAlertMapper implements Mapper<PaymentAlert, PaymentAlertResponse> {
    @Override
    public PaymentAlertResponse toDTO(PaymentAlert paymentAlert) {
        return new PaymentAlertResponse(
                paymentAlert.getId(),
                paymentAlert.getUser().getId(),
                paymentAlert.getSubscription().getSubscriptionCategory().getCompanyName() + " - " +
                paymentAlert.getSubscription().getSubscriptionCategory().getBillingType() + " - " +
                "$" + paymentAlert.getSubscription().getSubscriptionCategory().getPrice(),
                paymentAlert.getDate()
        );
    }
}
