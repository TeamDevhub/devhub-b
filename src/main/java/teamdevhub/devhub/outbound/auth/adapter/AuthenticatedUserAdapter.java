package teamdevhub.devhub.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.out.AuthenticatedUserResolver;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserAdapter implements AuthenticatedUserResolver {

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticatedUser getAuthenticatedUser(String email, String rawPassword) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, rawPassword));
        UserAuthentication userAuthentication = (UserAuthentication) authentication.getPrincipal();
        return userAuthentication.getUser();
    }
}