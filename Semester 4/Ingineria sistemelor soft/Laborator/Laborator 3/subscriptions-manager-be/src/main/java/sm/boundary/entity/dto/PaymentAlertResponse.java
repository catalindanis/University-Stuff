package sm.boundary.entity.dto;

import java.time.LocalDate;

public record PaymentAlertResponse(Long id, Long userId, String subscriptionDetail, LocalDate date) {
}
