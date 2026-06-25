package sm.boundary.entity.dto;

import lombok.Builder;
import sm.entity.BillingType;

@Builder
public record SubscriptionCategoryResponse(Long id, String companyName, BillingType billingType, double price) {
}
