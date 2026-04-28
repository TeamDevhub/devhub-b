package teamdevhub.devhub.outbound.auth.infrastructure.oauth.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthHttpClient;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.BearerAuthHeaderProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.DefaultHeaderProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.http.HttpResponse;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.google.config.GoogleOauthConfig;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.google.vo.GoogleTokenResponse;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.google.vo.GoogleUserResponse;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class GoogleOauthClientAdapter implements OauthClient {

    private final OauthHttpClient oauthHttpClient;
    private final IdentifierProvider identifierProvider;
    private final GoogleOauthConfig googleOauthConfig;

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return verificationProvider == VerificationProvider.GOOGLE;
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUri(URI.create(googleOauthConfig.getAuthorizationUri()))
                .queryParam("client_id", googleOauthConfig.getClientId())
                .queryParam("redirect_uri", googleOauthConfig.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "profile email")
                .queryParam("state", identifierProvider.generateIdentifier())
                .build()
                .toUriString();
    }

    @Override
    public OauthUser fetchUser(String code) {
        String accessToken = getAccessToken(code);

        HttpResponse<GoogleUserResponse> response = oauthHttpClient.get(googleOauthConfig.getUserInfoUri(), new BearerAuthHeaderProvider(accessToken), GoogleUserResponse.class);

        if (!response.is2xx() || response.body() == null) {
            throw new RuntimeException("구글 사용자 정보 조회 실패: " + response.rawBody());
        }

        GoogleUserResponse googleUser = response.body();

        return new OauthUser(googleUser.id(), VerificationProvider.GOOGLE, googleUser.email());
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("code", code);
        form.add("client_id", googleOauthConfig.getClientId());
        form.add("client_secret", googleOauthConfig.getClientSecret());
        form.add("redirect_uri", googleOauthConfig.getRedirectUri());
        form.add("grant_type", "authorization_code");

        HttpResponse<GoogleTokenResponse> googleToken = oauthHttpClient.postFormUrlEncoded(googleOauthConfig.getTokenUri(), form, new DefaultHeaderProvider(), GoogleTokenResponse.class);

        if (!googleToken.is2xx() || googleToken.body() == null || googleToken.body().access_token() == null) {
            throw new RuntimeException("구글 토큰 요청 실패: " + googleToken.rawBody());
        }

        return googleToken.body().access_token();
    }
}