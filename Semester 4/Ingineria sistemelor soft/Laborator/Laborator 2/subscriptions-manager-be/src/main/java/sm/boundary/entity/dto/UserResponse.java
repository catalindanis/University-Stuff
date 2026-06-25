package sm.boundary.entity.dto;

import lombok.Builder;
import sm.entity.UserRole;

@Builder
public record UserResponse(Long id, String firstName, String lastName, String email, UserRole role) {
}
