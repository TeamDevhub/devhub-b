package teamdevhub.devhub.fake.framework;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.core.auth.domain.UserCredential;

import java.util.Collections;

public class FakeAuthenticationManager implements AuthenticationManager {

    private final UserCredential userCredential;

    public FakeAuthenticationManager(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        return new UsernamePasswordAuthenticationToken(
                new UserAuthentication(userCredential),
                authentication.getCredentials(),
                Collections.emptyList()
        );
    }
}
