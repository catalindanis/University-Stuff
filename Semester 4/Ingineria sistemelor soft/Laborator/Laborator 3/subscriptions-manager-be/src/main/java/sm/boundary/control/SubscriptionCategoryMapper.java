package sm.boundary.control;

import jakarta.enterprise.context.ApplicationScoped;
import sm.boundary.entity.dto.SubscriptionCategoryResponse;
import sm.entity.SubscriptionCategory;

@ApplicationScoped
public class SubscriptionCategoryMapper implements Mapper<SubscriptionCategory, SubscriptionCategoryResponse> {
    @Override
    public SubscriptionCategoryResponse toDTO(SubscriptionCategory category) {
        return SubscriptionCategoryResponse.builder()
                .id(category.getId())
                .companyName(category.getCompanyName())
                .billingType(category.getBillingType())
                .price(category.getPrice())
                .build();
    }
}
