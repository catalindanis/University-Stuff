package ro.mpp2026.festivalmuzicajavafx.domain;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ShowFilter(
        String artistName,
        LocalDate date,
        String location,
        Integer remainingSeats
) {
}
