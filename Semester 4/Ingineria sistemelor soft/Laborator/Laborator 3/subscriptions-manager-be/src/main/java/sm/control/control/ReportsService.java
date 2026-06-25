package sm.control.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import sm.boundary.entity.dto.SubscriptionReportEntryResponse;
import sm.boundary.entity.dto.SubscriptionsCostResponse;
import sm.boundary.entity.dto.SubscriptionsReportResponse;
import sm.entity.BillingType;
import sm.entity.Subscription;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class ReportsService {

    private final SubscriptionsService subscriptionsService;

    @Transactional(Transactional.TxType.SUPPORTS)
    public SubscriptionsCostResponse calculateSubscriptionsCost() {
        return new SubscriptionsCostResponse(buildSubscriptionsReport().totalCost());
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public SubscriptionsReportResponse buildSubscriptionsReport() {
        List<Subscription> subscriptions = subscriptionsService.findAllSubscriptions();
        List<SubscriptionReportEntryResponse> entries = new ArrayList<>();
        double totalCost = 0;

        for (var subscription : subscriptions) {
            int multiplicationFactor = getMultiplicationFactor(subscription);
            double price = getSubscriptionPrice(subscription);
            double subscriptionCost = price * multiplicationFactor;
            totalCost += subscriptionCost;

            entries.add(new SubscriptionReportEntryResponse(
                    subscription.getId(),
                    subscription.getSubscriptionCategory().getCompanyName(),
                    subscription.getSubscriptionCategory().getBillingType(),
                    price,
                    subscription.getStartDate(),
                    multiplicationFactor,
                    subscriptionCost
            ));
        }

        return new SubscriptionsReportResponse(
                LocalDateTime.now(),
                entries.size(),
                totalCost,
                entries
        );
    }

    private int getMultiplicationFactor(Subscription subscription) {
        BillingType billingType = subscription.getSubscriptionCategory().getBillingType();
        LocalDate startDate = subscription.getStartDate();

        return 1 + switch (billingType) {
            case YEARLY -> calculateYearsPassed(startDate);
            case MONTHLY -> calculateMonthsPassed(startDate);
        };
    }

    private double getSubscriptionPrice(Subscription subscription) {
        return subscription.getSubscriptionCategory().getPrice();
    }

    private int calculateYearsPassed(LocalDate startDate) {
        return Math.max(0, (int) ChronoUnit.YEARS.between(startDate, LocalDate.now()));
    }

    private int calculateMonthsPassed(LocalDate startDate) {
        return Math.max(0, (int) ChronoUnit.MONTHS.between(startDate, LocalDate.now()));
    }
}
