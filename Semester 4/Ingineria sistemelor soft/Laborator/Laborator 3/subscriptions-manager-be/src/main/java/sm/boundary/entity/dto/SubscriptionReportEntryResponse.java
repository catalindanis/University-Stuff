package sm.boundary.entity.dto;

import sm.entity.BillingType;

import java.time.LocalDate;

public record SubscriptionReportEntryResponse(
        Long subscriptionId,
        String companyName,
        BillingType billingType,
        double unitPrice,
        LocalDate startDate,
        int billingPeriods,
        double totalCost
) {
}

