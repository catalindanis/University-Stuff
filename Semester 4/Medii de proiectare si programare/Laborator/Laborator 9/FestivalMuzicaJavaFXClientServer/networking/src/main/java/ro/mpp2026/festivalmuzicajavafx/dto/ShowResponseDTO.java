package ro.mpp2026.festivalmuzicajavafx.dto;

import java.io.Serializable;
import java.time.LocalDate;

public record ShowResponseDTO(Long id, String artistName, LocalDate date, String location, int remainingSeats) implements Serializable {
}
