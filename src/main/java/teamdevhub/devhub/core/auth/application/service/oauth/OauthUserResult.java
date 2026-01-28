package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.Builder;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;

@Builder
public record OauthUserResult(boolean loginAvailable, AuthenticatedUser authenticatedUser) {

    public static OauthUserResult success(AuthenticatedUser authenticatedUser) {
        return new OauthUserResult(true, authenticatedUser);
    }

    public static OauthUserResult requiresSignup() {
        return new OauthUserResult(false, null);
    }
}
