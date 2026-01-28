package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.Builder;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.shared.enums.SignupStatus;
import teamdevhub.devhub.shared.enums.TokenPrefix;

import java.util.Optional;

@Builder
public record OauthAuthResult(
        SignupStatus signupStatus,
        String accessToken,
        String refreshToken,
        String tempToken
) {

    public static OauthAuthResult loggedIn(AuthResult authResult) {
        return new OauthAuthResult(
                SignupStatus.COMPLETED,
                authResult.accessToken(),
                authResult.refreshToken(),
                null
        );
    }

    public static OauthAuthResult requiresSignup(String tempToken) {
        return new OauthAuthResult(
                SignupStatus.PENDING,
                null,
                null,
                tempToken
        );
    }

    public String toAuthorizationHeader() {
        return Optional.ofNullable(accessToken)
                .map(TokenPrefix.BEARER::withToken)
                .orElse(null);
    }
}
