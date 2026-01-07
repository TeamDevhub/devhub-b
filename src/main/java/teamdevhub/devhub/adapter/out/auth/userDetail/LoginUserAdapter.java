package teamdevhub.devhub.adapter.out.auth.userDetail;

import teamdevhub.devhub.domain.common.record.auth.LoginUser;
import teamdevhub.devhub.port.in.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserAdapter implements UserDetailsService {

    private final UserUseCase userUseCase;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LoginUser loginUser = userUseCase.getUserForAuth(email);
        return new LoginAuthentication(loginUser);
    }
}
