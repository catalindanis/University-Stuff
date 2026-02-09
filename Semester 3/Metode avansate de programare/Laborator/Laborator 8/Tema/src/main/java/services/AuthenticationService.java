package services;

import dto.UsersFilterDTO;
import models.User;
import utils.Encryption;
import utils.SessionData;
import utils.paging.Page;
import utils.paging.Pageable;

import java.util.Optional;

public class AuthenticationService {
    private static AuthenticationService instance;

    private AuthenticationService() {}

    public static AuthenticationService getInstance() {
        if (instance == null)
            instance = new AuthenticationService();

        return instance;
    }

    public SessionData login(String email, String password) {
        UsersFilterDTO filter = new UsersFilterDTO();
        filter.setEmail(Optional.of(email));
        filter.setPassword(Optional.of(Encryption.encrypt(password)));
        filter.setLoginAction(Optional.of(true));

        Page<User> page = UsersService.getInstance().getUsers(new Pageable(0, 1), filter);

        if(page.getElements().isEmpty())
            return null;

        return new SessionData(page.getElements().getFirst().getId());
    }
}
