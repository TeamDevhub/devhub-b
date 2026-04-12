package teamdevhub.devhub.outbound.auth.infrastructure.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.OauthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.kakao.KakaoTokenResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.vo.kakao.KakaoUserResponse;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Component
@RequiredArgsConstructor
public class KakaoOauthClientAdapter implements OauthClient {

    private final WebClient kakaoWebClient;
    private final IdentifierProvider identifierProvider;

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Override
    public boolean supports(VerificationProvider provider) {
        return provider == VerificationProvider.KAKAO;
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("state", identifierProvider.generateIdentifier())
                .build()
                .toUriString();
    }

    @Override
    public OauthUser fetchUser(String code) {

        String accessToken = fetchAccessToken(code);
        KakaoUserResponse user = fetchKakaoUser(accessToken);

        String email = extractEmail(user);

        return new OauthUser(
                String.valueOf(user.id()),
                VerificationProvider.KAKAO,
                email
        );
    }

    private String fetchAccessToken(String code) {

        KakaoTokenResponse response = kakaoWebClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret) // 콘솔 설정에 따라 필요
                        .with("redirect_uri", redirectUri)
                        .with("code", code))
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block();

        if (response == null || response.access_token() == null) {
            throw new RuntimeException("Kakao token 발급 실패");
        }

        return response.access_token();
    }

    private KakaoUserResponse fetchKakaoUser(String token) {

        return kakaoWebClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(KakaoUserResponse.class)
                .block();
    }

    private String extractEmail(KakaoUserResponse user) {

        if (user == null) {
            return null;
        }

        if (user.email() != null && !user.email().isBlank()) {
            return user.email();
        }

        return "kakao_" + user.id() + "@noemail.local";
    }
}