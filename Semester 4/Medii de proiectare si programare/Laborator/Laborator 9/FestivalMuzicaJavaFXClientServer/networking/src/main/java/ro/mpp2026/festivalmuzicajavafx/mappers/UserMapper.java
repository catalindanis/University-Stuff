package ro.mpp2026.festivalmuzicajavafx.mappers;

import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.dto.UserResponseDTO;

public class UserMapper implements Mapper<User, UserResponseDTO> {
    @Override
    public UserResponseDTO convert(User user) {
        return new UserResponseDTO(
                user.getId()
        );
    }
}
