package validators;

import models.User;
import services.UsersService;

public class UserValidator implements Validator<User> {
    @Override
    public boolean validate(User user) {
        if(user == null)
            return false;

        if(user.getUsername() == null || user.getUsername().isBlank())
            return false;

        if(user.getEmail() == null || user.getEmail().isBlank())
            return false;

        if(user.getPassword() == null || user.getPassword().isBlank())
            return false;

        return true;
    }
}
