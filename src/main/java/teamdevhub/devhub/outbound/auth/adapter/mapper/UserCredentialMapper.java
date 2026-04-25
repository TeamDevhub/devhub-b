package teamdevhub.devhub.outbound.auth.adapter.mapper;

import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.outbound.auth.adapter.entity.OAuthCredentialEntity;
import teamdevhub.devhub.shared.enums.VerificationProvider;

public class UserCredentialMapper {

    public static AuthenticatedUser toAuthenticatedUser(EmailCredentialEntity entity) {
        return AuthenticatedUser.of(
                entity.getUserGuid(),
                entity.getEmail(),
                entity.getUserRole()
        );
    }

    public static AuthenticatedUser toAuthenticatedUser(OAuthCredentialEntity entity) {
        return AuthenticatedUser.of(
                entity.getUserGuid(),
                entity.getOauthId(),
                entity.getUserRole()
        );
    }

    public static EmailUserCredential toEmailUserCredential(EmailCredentialEntity entity) {
        return new EmailUserCredential(
                entity.getUserGuid(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getUserRole()
        );
    }

    public static EmailCredentialEntity toEmailCredentialEntity(EmailUserCredential domain) {
        return EmailCredentialEntity.builder()
                .userGuid(domain.getUserGuid())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .userRole(domain.getUserRole())
                .build();
    }

    public static EmailCredentialEntity toEmailCredentialEntity(
            AuthenticatedUser user,
            String encryptedPassword
    ) {
        return EmailCredentialEntity.builder()
                .userGuid(user.userGuid())
                .email(user.loginId())
                .password(encryptedPassword)
                .userRole(user.userRole())
                .build();
    }

    public static OAuthCredentialEntity toOAuthCredentialEntity(
            AuthenticatedUser user,
            VerificationProvider verificationProvider,
            String oauthId
    ) {
        return OAuthCredentialEntity.builder()
                .userGuid(user.userGuid())
                .provider(verificationProvider)
                .oauthId(oauthId)
                .userRole(user.userRole())
                .build();
    }
}
