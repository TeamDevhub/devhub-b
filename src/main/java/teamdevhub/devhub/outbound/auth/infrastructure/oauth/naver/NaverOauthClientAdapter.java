package teamdevhub.devhub.outbound.auth.infrastructure.oauth.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthHttpClient;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.BearerAuthHeaderProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.DefaultHeaderProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.HttpResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.naver.config.NaverOauthConfig;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.naver.vo.NaverTokenResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.naver.vo.NaverUserResponse;
import teamdevhub.devhub.shared.enums.VerificationProvider;

@Component
@RequiredArgsConstructor
public class NaverOauthClientAdapter implements OauthClient {

    private final OauthHttpClient oauthHttpClient;
    private final IdentifierProvider identifierProvider;
    private final NaverOauthConfig naverOauthConfig;

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return verificationProvider == VerificationProvider.NAVER;
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUriString(naverOauthConfig.getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", naverOauthConfig.getClientId())
                .queryParam("redirect_uri", naverOauthConfig.getRedirectUri())
                .queryParam("state", identifierProvider.generateIdentifier())
                .build()
                .toUriString();
    }

    @Override
    public OauthUser fetchUser(String code) {
        String accessToken = getAccessToken(code);

        HttpResponse<NaverUserResponse> response = oauthHttpClient.get(
                naverOauthConfig.getUserInfoUri(),
                new BearerAuthHeaderProvider(accessToken),
                NaverUserResponse.class
        );

        if (!response.is2xx() || response.body() == null) {
            throw new RuntimeException("Naver 사용자 조회 실패: " + response.rawBody());
        }

        NaverUserResponse naverUser = response.body();

        if (naverUser.response() == null || naverUser.response().id() == null) {
            throw new RuntimeException("Naver 사용자 응답 이상: " + response.rawBody());
        }

        String email = extractEmail(naverUser);

        return new OauthUser(
                naverUser.response().id(),
                VerificationProvider.NAVER,
                email
        );
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("grant_type", "authorization_code");
        form.add("client_id", naverOauthConfig.getClientId());
        form.add("client_secret", naverOauthConfig.getClientSecret());
        form.add("code", code);
        form.add("state", identifierProvider.generateIdentifier());

        HttpResponse<NaverTokenResponse> naverToken = oauthHttpClient.postFormUrlEncoded(
                naverOauthConfig.getTokenUri(),
                form,
                new DefaultHeaderProvider(),
                NaverTokenResponse.class
        );

        if (!naverToken.is2xx() || naverToken.body() == null || naverToken.body().access_token() == null) {
            throw new RuntimeException("Naver 토큰 요청 실패: " + naverToken.rawBody());
        }

        return naverToken.body().access_token();
    }

    private String extractEmail(NaverUserResponse user) {
        if (user.response() != null && user.response().email() != null && !user.response().email().isBlank()) {
            return user.response().email();
        }

        return "naver_" + user.response().id() + "@local";
    }
}