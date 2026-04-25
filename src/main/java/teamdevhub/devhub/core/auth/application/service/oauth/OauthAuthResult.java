package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.Builder;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.outbound.auth.infrastructure.token.TokenPrefix;
import teamdevhub.devhub.shared.enums.ErrorCode;

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
        if (accessToken == null) {
            throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);
        }
        return TokenPrefix.BEARER.withToken(accessToken);
    }
}
