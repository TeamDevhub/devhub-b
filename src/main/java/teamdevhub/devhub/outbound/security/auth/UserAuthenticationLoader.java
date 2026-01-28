package teamdevhub.devhub.outbound.security.auth;

import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.user.port.out.UserRepository;

@Service
@RequiredArgsConstructor
public class UserAuthenticationLoader implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthenticatedUser authenticatedUser = userRepository.findAuthenticatedUserByEmail(email);
        return new UserAuthentication(authenticatedUser);
    }
}
