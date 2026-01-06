package teamdevhub.devhub.adapter.out.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.port.out.provider.AuthenticatedUserProvider;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserUtil implements AuthenticatedUserProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}