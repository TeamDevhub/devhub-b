package teamdevhub.devhub.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.outbound.auth.persistence.JpaEmailCredentialRepository;
import teamdevhub.devhub.outbound.auth.persistence.JpaOauthCredentialRepository;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCredentialAdapter implements UserCredentialRepository {

    private final JpaEmailCredentialRepository jpaEmailCredentialRepository;
    private final JpaOauthCredentialRepository jpaOauthCredentialRepository;

    @Override
    public Optional<UserCredential> findEmailUserCredentialByUserGuid(String userGuid) {

        return Optional.ofNullable(jpaEmailCredentialRepository.findByUserGuid(userGuid)
                .map(c -> UserCredential.of(
                        c.getUserGuid(),
                        c.getEmail(),
                        c.getUserRole()
                ))
                .orElseGet(() ->
                        jpaOauthCredentialRepository.findByUserGuid(userGuid)
                                .map(c -> UserCredential.of(
                                        c.getUserGuid(),
                                        c.getOauthId(),
                                        c.getUserRole()
                                ))
                                .orElseThrow(() -> new IllegalArgumentException("user not found"))
                ));
    }

    public Optional<UserCredential> findEmailUserCredentialByEmail(String email) {

        return Optional.ofNullable(jpaEmailCredentialRepository.findByEmail(email)
                .map(c -> UserCredential.of(
                        c.getUserGuid(),
                        c.getEmail(),
                        c.getUserRole()
                ))
                .orElseThrow(() -> new IllegalArgumentException("user not found")));
    }

    @Override
    public Optional<UserCredential> findOAuthUserCredentialByOAuth(VerificationProvider provider, String oauthId) {

        return Optional.ofNullable(jpaOauthCredentialRepository.findByProviderAndOauthId(provider, oauthId)
                .map(c -> UserCredential.of(
                        c.getUserGuid(),
                        c.getOauthId(),
                        c.getUserRole()
                ))
                .orElseThrow(() -> new IllegalArgumentException("user not found")));
    }
}
