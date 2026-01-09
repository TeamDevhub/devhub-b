package teamdevhub.devhub.small.mock.infrastructure;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import teamdevhub.devhub.common.web.security.auth.UserAuthentication;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;

import java.util.Collections;

public class FakeAuthenticationManager implements AuthenticationManager {

    private final AuthenticatedUser authenticatedUser;

    public FakeAuthenticationManager(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        return new UsernamePasswordAuthenticationToken(
                new UserAuthentication(authenticatedUser),
                authentication.getCredentials(),
                Collections.emptyList()
        );
    }
}
