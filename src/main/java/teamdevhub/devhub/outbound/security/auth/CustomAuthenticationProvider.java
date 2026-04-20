package teamdevhub.devhub.outbound.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.domain.vo.EmailCredential;
import teamdevhub.devhub.core.auth.port.out.EmailCredentialRepository;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.out.UserRepository;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final EmailCredentialRepository emailCredentialRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {

        String email = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        EmailCredential credential = emailCredentialRepository.findByEmail(email);

        if (!passwordEncoder.matches(rawPassword, credential.password())) {
            throw new BadCredentialsException("invalid password");
        }

        User user = userRepository.findByUserGuid(credential.userGuid());

        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(
                user.getUserGuid(),
                credential.email(),
                user.getUserRole()
        );

        return new UsernamePasswordAuthenticationToken(
                new UserAuthentication(authenticatedUser),
                null,
                List.of(new SimpleGrantedAuthority(user.getUserRole().getAuthority()))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
