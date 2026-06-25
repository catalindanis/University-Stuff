package sm.boundary.entity.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PaymentAlertRequest(@NotNull Long subscriptionId, LocalDate date) {
}
