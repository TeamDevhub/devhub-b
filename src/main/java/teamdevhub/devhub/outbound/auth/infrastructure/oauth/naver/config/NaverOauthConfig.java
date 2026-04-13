package teamdevhub.devhub.outbound.auth.infrastructure.oauth.naver.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthProviderConfig;

@Getter
@Component
public class NaverOauthConfig implements OauthProviderConfig {

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value("${oauth.naver.client-secret}")
    private String clientSecret;

    @Value("${oauth.naver.redirect-uri}")
    private String redirectUri;

    @Override
    public String getAuthorizationUri() {
        return "https://kauth.naver.com/oauth/authorize";
    }

    @Override
    public String getTokenUri() {
        return "https://kauth.naver.com/oauth/token";
    }

    @Override
    public String getUserInfoUri() {
        return "https://kapi.naver.com/v2/user/me";
    }
}