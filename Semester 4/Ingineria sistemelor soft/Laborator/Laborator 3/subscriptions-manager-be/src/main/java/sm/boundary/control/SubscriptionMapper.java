package sm.boundary.control;

import jakarta.enterprise.context.ApplicationScoped;
import sm.boundary.entity.dto.SubscriptionResponse;
import sm.entity.Subscription;

@ApplicationScoped
public class SubscriptionMapper implements Mapper<Subscription, SubscriptionResponse> {
    @Override
    public SubscriptionResponse toDTO(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .authorId(subscription.getAuthor() != null ? subscription.getAuthor().getId() : null)
                .subscriptionCategoryId(subscription.getSubscriptionCategory() != null ? subscription.getSubscriptionCategory().getId() : null)
                .startDate(subscription.getStartDate())
                .build();
    }
}

