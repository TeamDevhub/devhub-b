package teamdevhub.devhub.outbound.auth.infrastructure.oauth;

public interface OauthProviderConfig {

    String getClientId();
    String getClientSecret();
    String getRedirectUri();
    String getAuthorizationUri();
    String getTokenUri();
    String getUserInfoUri();
}