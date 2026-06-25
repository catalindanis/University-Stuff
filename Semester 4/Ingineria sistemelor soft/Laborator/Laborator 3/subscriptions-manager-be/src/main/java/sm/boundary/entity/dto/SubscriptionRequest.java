package sm.boundary.entity.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SubscriptionRequest(@NotNull Long subscriptionCategoryId, @NotNull LocalDate startDate) {
}

