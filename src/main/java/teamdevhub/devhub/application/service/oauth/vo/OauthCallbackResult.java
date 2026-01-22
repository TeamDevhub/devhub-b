package teamdevhub.devhub.application.service.oauth.vo;

import lombok.Builder;
import teamdevhub.devhub.common.enums.SignupStatus;

@Builder
public record OauthCallbackResult(SignupStatus signupStatus, String tempToken) {

    public static OauthCallbackResult existedUser(String tempToken) {
        return new OauthCallbackResult(SignupStatus.COMPLETED, tempToken);
    }

    public static OauthCallbackResult requiresSignupUser(String tempToken) {
        return new OauthCallbackResult(SignupStatus.PENDING, tempToken);
    }
}
