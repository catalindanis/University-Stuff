package sm.boundary.entity.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@NotNull
public record RegisterRequest(@NotBlank(message = "First name can not be empty") String firstName,
                              @NotBlank(message = "Last name can not be empty") String lastName,
                              @NotBlank @Email(message = "A valid email must be entered") String email,
                              @NotBlank @Size(min = 8, message = "Password must be at least 8 characters long") String password) {
}
