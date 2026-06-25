package sm.boundary.entity.dto;
import java.time.LocalDateTime;
import java.util.List;
public record SubscriptionsReportResponse(
        LocalDateTime generatedAt,
        int subscriptionsCount,
        double totalCost,
        List<SubscriptionReportEntryResponse> subscriptions
) {
}