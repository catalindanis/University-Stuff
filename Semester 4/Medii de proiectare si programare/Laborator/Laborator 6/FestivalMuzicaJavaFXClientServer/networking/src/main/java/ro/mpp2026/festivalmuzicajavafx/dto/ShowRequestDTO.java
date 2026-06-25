package ro.mpp2026.festivalmuzicajavafx.dto;

import java.io.Serializable;
import java.time.LocalDate;

public record ShowRequestDTO(String artistName, LocalDate date, String location, int remainingSeats) implements Serializable {
}
