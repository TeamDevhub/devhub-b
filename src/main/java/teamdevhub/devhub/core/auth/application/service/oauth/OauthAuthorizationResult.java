package teamdevhub.devhub.core.auth.application.service.oauth;

public record OauthAuthorizationResult(String url, String state) {

    public static OauthAuthorizationResult of(String url, String state) {
        return new OauthAuthorizationResult(url, state);
    }
}
