package teamdevhub.devhub.outbound.auth.infrastructure.oauth.google;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthHttpClient;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
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
    public boolean supports(VerificationProvider provider) {
        return provider == VerificationProvider.GOOGLE;
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

        String token = getAccessToken(code);

        GoogleUserResponse user = oauthHttpClient.get(
                googleOauthConfig.getUserInfoUri(),
                h -> h.setBearerAuth(token),
                GoogleUserResponse.class
        );

        if (user == null) {
            throw new RuntimeException("구글 사용자 정보 조회 실패");
        }

        return new OauthUser(
                user.id(),
                VerificationProvider.GOOGLE,
                user.email()
        );
    }

    private String getAccessToken(String code) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", googleOauthConfig.getClientId());
        form.add("client_secret", googleOauthConfig.getClientSecret());
        form.add("redirect_uri", googleOauthConfig.getRedirectUri());
        form.add("grant_type", "authorization_code");

        GoogleTokenResponse res = oauthHttpClient.postForm(
                googleOauthConfig.getTokenUri(),
                form,
                h -> {},
                GoogleTokenResponse.class
        );

        if (res == null || res.access_token() == null) {
            throw new RuntimeException("구글 token 실패");
        }

        return res.access_token();
    }
}