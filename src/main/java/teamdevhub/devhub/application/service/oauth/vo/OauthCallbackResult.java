package teamdevhub.devhub.application.service.oauth.vo;

import lombok.Builder;

@Builder
public record OauthCallbackResult(String tempToken) {

    public static OauthCallbackResult existedUser(String tempToken) {
        return new OauthCallbackResult(tempToken);
    }

    public static OauthCallbackResult requiresSignupUser(String tempToken) {
        return new OauthCallbackResult(tempToken);
    }
}
