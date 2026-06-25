package sm.boundary.entity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sm.entity.BillingType;

public record SubscriptionCategoryUpdateRequest(@NotNull Long id, String companyName, BillingType billingType, @Positive Double price) {
}
