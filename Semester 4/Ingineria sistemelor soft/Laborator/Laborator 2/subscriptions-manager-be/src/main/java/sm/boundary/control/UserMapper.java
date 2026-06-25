package sm.boundary.control;

import jakarta.enterprise.context.ApplicationScoped;
import sm.boundary.entity.dto.UserResponse;
import sm.entity.User;

@ApplicationScoped
public class UserMapper implements Mapper<User, UserResponse> {
    @Override
    public UserResponse toDTO(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
