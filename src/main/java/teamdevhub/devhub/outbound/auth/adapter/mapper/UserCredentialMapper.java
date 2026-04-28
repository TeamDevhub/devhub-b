package teamdevhub.devhub.outbound.auth.adapter.mapper;

import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.outbound.auth.adapter.entity.OAuthCredentialEntity;
import teamdevhub.devhub.shared.enums.VerificationProvider;

public class UserCredentialMapper {

    public static AuthenticatedUser toAuthenticatedUser(EmailCredentialEntity emailCredentialEntity) {
        return AuthenticatedUser.of(
                emailCredentialEntity.getUserGuid(),
                emailCredentialEntity.getEmail(),
                emailCredentialEntity.getUserRole()
        );
    }

    public static AuthenticatedUser toAuthenticatedUser(OAuthCredentialEntity oAuthCredentialEntity) {
        return AuthenticatedUser.of(
                oAuthCredentialEntity.getUserGuid(),
                oAuthCredentialEntity.getOauthId(),
                oAuthCredentialEntity.getUserRole()
        );
    }

    public static EmailUserCredential toEmailUserCredential(EmailCredentialEntity emailCredentialEntity) {
        return new EmailUserCredential(
                emailCredentialEntity.getUserGuid(),
                emailCredentialEntity.getEmail(),
                emailCredentialEntity.getPassword(),
                emailCredentialEntity.getUserRole()
        );
    }

    public static EmailCredentialEntity toEmailCredentialEntity(EmailUserCredential emailUserCredential) {
        return EmailCredentialEntity.builder()
                .userGuid(emailUserCredential.getUserGuid())
                .email(emailUserCredential.getEmail())
                .password(emailUserCredential.getPassword())
                .userRole(emailUserCredential.getUserRole())
                .build();
    }

    public static EmailCredentialEntity toEmailCredentialEntity(
            AuthenticatedUser authenticatedUser,
            String encryptedPassword
    ) {
        return EmailCredentialEntity.builder()
                .userGuid(authenticatedUser.userGuid())
                .email(authenticatedUser.loginId())
                .password(encryptedPassword)
                .userRole(authenticatedUser.userRole())
                .build();
    }

    public static OAuthCredentialEntity toOAuthCredentialEntity(
            AuthenticatedUser authenticatedUser,
            VerificationProvider verificationProvider,
            String oauthId
    ) {
        return OAuthCredentialEntity.builder()
                .userGuid(authenticatedUser.userGuid())
                .provider(verificationProvider)
                .oauthId(oauthId)
                .userRole(authenticatedUser.userRole())
                .build();
    }
}
