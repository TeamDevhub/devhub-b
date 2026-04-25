package teamdevhub.devhub.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.outbound.auth.adapter.entity.OAuthCredentialEntity;
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
        return jpaEmailCredentialRepository.findByUserGuid(userGuid)
                .map(this::toUserCredential);
    }

    @Override
    public Optional<UserCredential> findEmailUserCredentialByEmail(String email) {
        return jpaEmailCredentialRepository.findByEmail(email)
                .map(this::toUserCredential);
    }

    @Override
    public Optional<UserCredential> findOAuthUserCredentialByOAuth(VerificationProvider provider, String oauthId) {
        return jpaOauthCredentialRepository.findByProviderAndOauthId(provider, oauthId)
                .map(this::toUserCredential);
    }

    @Override
    public void saveEmailUserCredential(UserCredential userCredential, String encryptedPassword) {
        jpaEmailCredentialRepository.save(
                EmailCredentialEntity.builder()
                        .userGuid(userCredential.userGuid())
                        .email(userCredential.loginId())
                        .password(encryptedPassword)
                        .userRole(userCredential.userRole())
                        .build()
        );
    }

    @Override
    public void saveOAuthUserCredential(UserCredential userCredential, VerificationProvider verificationProvider, String oauthId) {
        jpaOauthCredentialRepository.save(
                OAuthCredentialEntity.builder()
                        .userGuid(userCredential.userGuid())
                        .provider(verificationProvider)
                        .oauthId(oauthId)
                        .userRole(userCredential.userRole())
                        .build()
        );
    }

    private UserCredential toUserCredential(EmailCredentialEntity emailCredentialEntity) {
        return UserCredential.of(
                emailCredentialEntity.getUserGuid(),
                emailCredentialEntity.getEmail(),
                emailCredentialEntity.getUserRole()
        );
    }

    private UserCredential toUserCredential(OAuthCredentialEntity oAuthCredentialEntity) {
        return UserCredential.of(
                oAuthCredentialEntity.getUserGuid(),
                oAuthCredentialEntity.getOauthId(),
                oAuthCredentialEntity.getUserRole()
        );
    }
}