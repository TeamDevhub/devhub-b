package teamdevhub.devhub.application.service.oauth.vo;

import lombok.Builder;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;

@Builder
public record OauthUserResult(boolean loginAvailable, AuthenticatedUser authenticatedUser) {

    public static OauthUserResult success(AuthenticatedUser authenticatedUser) {
        return new OauthUserResult(true, authenticatedUser);
    }

    public static OauthUserResult requiresSignup() {
        return new OauthUserResult(false, null);
    }
}
