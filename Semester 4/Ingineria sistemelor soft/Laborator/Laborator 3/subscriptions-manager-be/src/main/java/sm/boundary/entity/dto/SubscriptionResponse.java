package sm.boundary.entity.dto;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public record SubscriptionResponse(Long id, Long authorId, Long subscriptionCategoryId, LocalDate startDate) {
}

