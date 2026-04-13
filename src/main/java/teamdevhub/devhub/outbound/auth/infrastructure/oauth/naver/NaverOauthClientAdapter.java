package teamdevhub.devhub.outbound.auth.infrastructure.oauth.naver;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.naver.vo.NaverTokenResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.naver.vo.NaverUserResponse;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Component
@RequiredArgsConstructor
public class NaverOauthClientAdapter implements OauthClient {

    private final WebClient naverWebClient;
    private final IdentifierProvider identifierProvider;

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value("${oauth.naver.client-secret}")
    private String clientSecret;

    @Value("${oauth.naver.redirect-uri}")
    private String redirectUri;

    @Override
    public boolean supports(VerificationProvider provider) {
        return provider == VerificationProvider.NAVER;
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromHttpUrl("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("state", identifierProvider.generateIdentifier())
                .build()
                .toUriString();
    }

    @Override
    public OauthUser fetchUser(String code) {

        String accessToken = fetchAccessToken(code);
        NaverUserResponse user = fetchNaverUser(accessToken);

        String email = extractEmail(user);

        return new OauthUser(
                user.id(),
                VerificationProvider.NAVER,
                email
        );
    }

    private String fetchAccessToken(String code) {

        NaverTokenResponse response = naverWebClient.post()
                .uri("https://nid.naver.com/oauth2.0/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("code", code)
                        .with("state", identifierProvider.generateIdentifier()))
                .retrieve()
                .bodyToMono(NaverTokenResponse.class)
                .block();

        if (response == null || response.access_token() == null) {
            throw new RuntimeException("Naver token 발급 실패");
        }

        return response.access_token();
    }

    private NaverUserResponse fetchNaverUser(String token) {

        return naverWebClient.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(NaverUserResponse.class)
                .block();
    }

    private String extractEmail(NaverUserResponse user) {

        if (user == null) {
            return null;
        }

        if (user.email() != null && !user.email().isBlank()) {
            return user.email();
        }

        return "naver_" + user.id() + "@noemail.local";
    }
}