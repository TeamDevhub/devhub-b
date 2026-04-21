package teamdevhub.devhub.core.auth.application.service.oauth;

import lombok.Builder;
import teamdevhub.devhub.core.auth.domain.UserCredential;

@Builder
public record OauthUserResult(boolean loginAvailable, UserCredential userCredential) {

    public static OauthUserResult success(UserCredential userCredential) {
        return new OauthUserResult(true, userCredential);
    }

    public static OauthUserResult requiresSignup() {
        return new OauthUserResult(false, null);
    }
}
