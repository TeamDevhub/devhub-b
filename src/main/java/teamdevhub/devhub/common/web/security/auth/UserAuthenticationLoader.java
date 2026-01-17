package teamdevhub.devhub.common.web.security.auth;

import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.AuthUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationLoader implements UserDetailsService {

    private final AuthUserUseCase authUserUseCase;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthenticatedUser authenticatedUser = authUserUseCase.getUserForLogin(email);
        return new UserAuthentication(authenticatedUser);
    }
}
