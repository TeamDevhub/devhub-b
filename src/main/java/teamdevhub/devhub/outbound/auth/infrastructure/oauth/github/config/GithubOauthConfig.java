package teamdevhub.devhub.outbound.auth.infrastructure.oauth.github.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthProviderConfig;

@Getter
@Component
public class GithubOauthConfig implements OauthProviderConfig {

    @Value("${oauth.github.client-id}")
    private String clientId;

    @Value("${oauth.github.client-secret}")
    private String clientSecret;

    @Value("${oauth.github.redirect-uri}")
    private String redirectUri;

    @Override
    public String getAuthorizationUri() {
        return "https://github.com/login/oauth/authorize";
    }

    @Override
    public String getTokenUri() {
        return "https://github.com/login/oauth/access_token";
    }

    @Override
    public String getUserInfoUri() {
        return "https://api.github.com/user";
    }

    public String getEmailUri() {
        return "https://api.github.com/user/emails";
    }
}