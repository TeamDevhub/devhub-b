package teamdevhub.devhub.common.web.security.auth;

import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticatedUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationLoader implements UserDetailsService {

    private final AuthenticatedUserUseCase authenticatedUserUseCase;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthenticatedUser authenticatedUser = authenticatedUserUseCase.getUserForLogin(email);
        return new UserAuthentication(authenticatedUser);
    }
}
