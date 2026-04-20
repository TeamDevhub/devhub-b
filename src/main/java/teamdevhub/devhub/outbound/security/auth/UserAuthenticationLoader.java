package teamdevhub.devhub.outbound.security.auth;

import teamdevhub.devhub.core.auth.domain.vo.EmailCredential;
import teamdevhub.devhub.core.auth.port.out.EmailCredentialRepository;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.user.port.out.UserRepository;

@Service
@RequiredArgsConstructor
public class UserAuthenticationLoader implements UserDetailsService {

    private final EmailCredentialRepository emailCredentialRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EmailCredential emailCredential = emailCredentialRepository.findByEmail(email);
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(
                emailCredential.userGuid(),
                emailCredential.email(),
                userRepository.findByUserGuid(emailCredential.userGuid()).getUserRole());
        return new UserAuthentication(authenticatedUser);
    }
}
