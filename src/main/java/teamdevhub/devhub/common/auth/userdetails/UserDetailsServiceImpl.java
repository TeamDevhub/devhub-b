package teamdevhub.devhub.common.auth.userdetails;

import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;
import teamdevhub.devhub.port.in.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserUseCase userUseCase;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthenticatedUser authenticatedUser = userUseCase.getUserForAuth(email);
        return new UserDetailsImpl(authenticatedUser);
    }
}
