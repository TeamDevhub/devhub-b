package teamdevhub.devhub.core.auth.application.service.vo;

import lombok.Builder;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;

@Builder
public record OauthUserResult(boolean loginAvailable, AuthenticatedUser authenticatedUser) {

    public static OauthUserResult success(AuthenticatedUser authenticatedUser) {
        return new OauthUserResult(true, authenticatedUser);
    }

    public static OauthUserResult requiresSignup() {
        return new OauthUserResult(false, null);
    }
}
