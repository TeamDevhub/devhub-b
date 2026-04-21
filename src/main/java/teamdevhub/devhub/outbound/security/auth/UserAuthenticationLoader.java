package teamdevhub.devhub.outbound.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.core.auth.domain.vo.user.EmailUserCredential;
import teamdevhub.devhub.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.core.auth.domain.UserCredential;

@Service
@RequiredArgsConstructor
public class UserAuthenticationLoader implements UserDetailsService {

    private final EmailUserCredentialRepository emailUserCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        EmailUserCredential emailUserCredential = emailUserCredentialRepository.findByEmail(email).orElseThrow();

        UserCredential userCredential = UserCredential.of(
                emailUserCredential.userGuid(),
                emailUserCredential.email(),
                emailUserCredential.userRole()
        );

        return new UserAuthentication(userCredential);
    }
}
