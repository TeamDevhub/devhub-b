package teamdevhub.devhub.adapter.out.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.common.web.security.auth.UserAuthentication;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserProvider;

@Component
@RequiredArgsConstructor
public class SpringSecurityAuthenticationAdapter implements AuthenticatedUserProvider {

    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticatedUser getAuthenticatedUser(String email, String rawPassword) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, rawPassword));
        UserAuthentication userAuthentication = (UserAuthentication) authentication.getPrincipal();
        return userAuthentication.getUser();
    }
}