package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.Builder;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Builder
public record OauthUserResult(boolean loginAvailable, AuthenticatedUser authenticatedUser) {

    public static OauthUserResult success(AuthenticatedUser authenticatedUser) {
        return new OauthUserResult(true, authenticatedUser);
    }

    public static OauthUserResult requiresSignup() {
        return new OauthUserResult(false, null);
    }

    public AuthenticatedUser requireAuthenticatedUser() {
        if (!loginAvailable) {
            throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);
        }
        return authenticatedUser;
    }
}
