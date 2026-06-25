package security;

import entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import repositories.PlayersRepository;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PlayersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Player user = usersRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + nickname));

        return new org.springframework.security.core.userdetails.User(
                user.getNickname(),
                "{noop}",
                new ArrayList<>()
        );
    }
}

