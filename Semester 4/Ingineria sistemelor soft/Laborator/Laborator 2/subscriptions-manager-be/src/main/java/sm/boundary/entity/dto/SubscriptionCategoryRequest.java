package sm.boundary.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import sm.entity.BillingType;

public record SubscriptionCategoryRequest(@NotBlank String companyName, @NotNull BillingType billingType, @NotNull @Positive Double price) {
}
