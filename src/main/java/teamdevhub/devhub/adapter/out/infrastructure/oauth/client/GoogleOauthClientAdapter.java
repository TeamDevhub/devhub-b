package teamdevhub.devhub.adapter.out.infrastructure.oauth.client;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.out.oauth.OauthClient;
import teamdevhub.devhub.port.out.provider.IdentifierProvider;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleOauthClientAdapter implements OauthClient {

    private final IdentifierProvider identifierProvider;

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return verificationProvider == VerificationProvider.GOOGLE;
    }

    @Override
    public String getAuthorizationUrl() {
        return new GoogleAuthorizationCodeRequestUrl(clientId, redirectUri, List.of("profile", "email"))
                .setState(identifierProvider.generateIdentifier())
                .build();
    }

    /**
     * Oauth 로 인증된 사용자를 가져오는 함수
     * @param
     * @return
     */
    @Override
    public OauthUser fetchUser(String code) {
        String value = clientSecret;
        return new OauthUser("google-id", VerificationProvider.GOOGLE,  "test@gmail.com");
    }
}
