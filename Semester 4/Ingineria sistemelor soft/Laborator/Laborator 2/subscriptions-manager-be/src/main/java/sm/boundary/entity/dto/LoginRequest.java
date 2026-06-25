package sm.boundary.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@NotNull
public record LoginRequest(@NotBlank @Email(message = "A valid email must be entered") String email,
                           @NotBlank String password) {
}
