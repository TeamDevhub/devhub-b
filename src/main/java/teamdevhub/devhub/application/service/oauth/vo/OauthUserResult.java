package teamdevhub.devhub.application.service.oauth.vo;

import lombok.Builder;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;

@Builder
public record OauthUserResult(boolean loginAvailable, AuthenticatedUser authenticatedUser, String tempToken) {

    public static OauthUserResult success(AuthenticatedUser authenticatedUser) {
        return new OauthUserResult(true, authenticatedUser, null);
    }

    public static OauthUserResult requiresSignup(String tempToken) {
        return new OauthUserResult(false, null, tempToken);
    }
}
