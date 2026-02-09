package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.Duck;
import models.UserType;
import services.UsersService;

@Getter
@AllArgsConstructor
public class SessionData {
    private Long userId;

    public boolean isAdmin() {
        return UsersService.getInstance().getById(userId).getUserType() == UserType.admin;
    }

    public boolean isDuck() {
        return UsersService.getInstance().getById(userId) instanceof Duck;
    }
}


