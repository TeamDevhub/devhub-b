package teamdevhub.devhub.outbound.auth.infrastructure.oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.GoogleTokenResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.GoogleUserResponse;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.OauthUser;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public OauthUser fetchUser(String code) {

        GoogleTokenResponse tokenResponse = WebClient.create()
                .post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("code", code)
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri)
                        .with("grant_type", "authorization_code"))
                .retrieve()
                .bodyToMono(GoogleTokenResponse.class)
                .block();

        if (tokenResponse == null) {
            throw new RuntimeException("구글 access token 요청 실패");
        }

        GoogleUserResponse userResponse = WebClient.create()
                .get()
                .uri("https://www.googleapis.com/oauth2/v2/userinfo")
                .headers(headers -> headers.setBearerAuth(tokenResponse.access_token()))
                .retrieve()
                .bodyToMono(GoogleUserResponse.class)
                .block();

        if (userResponse == null) {
            throw new RuntimeException("구글 사용자 정보 조회 실패");
        }

        log.info("oauth 정보 id {}, email {}, boolean {}, name {}", userResponse.id(), userResponse.email(), userResponse.verified_email(), userResponse.name());

        return new OauthUser(
                userResponse.id(),
                VerificationProvider.GOOGLE,
                userResponse.email()
        );
    }
}
