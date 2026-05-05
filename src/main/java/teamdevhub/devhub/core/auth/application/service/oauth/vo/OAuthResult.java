package teamdevhub.devhub.core.auth.application.service.oauth.vo;

import lombok.Builder;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.outbound.auth.infrastructure.token.TokenPrefix;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Builder
public record OAuthResult(
        SignupStatus signupStatus,
        String accessToken,
        String refreshToken,
        String tempToken
) {

    public static OAuthResult loggedIn(AuthResult authResult) {
        return new OAuthResult(
                SignupStatus.COMPLETED,
                authResult.accessToken(),
                authResult.refreshToken(),
                null
        );
    }

    public static OAuthResult requiresSignup(String tempToken) {
        return new OAuthResult(
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
