package ro.mpp2026.festivalmuzicajavafx.dto;

import java.io.Serializable;

public record UserRequestDTO(String email, String password) implements Serializable {
}
