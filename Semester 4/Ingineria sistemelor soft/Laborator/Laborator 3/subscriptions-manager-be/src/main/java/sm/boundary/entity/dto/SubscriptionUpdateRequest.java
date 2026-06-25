package sm.boundary.entity.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record SubscriptionUpdateRequest(@NotNull Long id, Long subscriptionCategoryId, LocalDate startDate) {
}

