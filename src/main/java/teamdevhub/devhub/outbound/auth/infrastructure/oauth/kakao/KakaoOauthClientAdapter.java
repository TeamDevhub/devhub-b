package teamdevhub.devhub.outbound.auth.infrastructure.oauth.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthHttpClient;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.kakao.config.KakaoOauthConfig;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.kakao.vo.*;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class KakaoOauthClientAdapter implements OauthClient {

    private final OauthHttpClient oauthHttpClient;
    private final KakaoOauthConfig kakaoOauthConfig;
    private final IdentifierProvider identifierProvider;

    @Override
    public boolean supports(VerificationProvider provider) {
        return provider == VerificationProvider.KAKAO;
    }

    @Override
    public String getAuthorizationUrl() {
        return UriComponentsBuilder
                .fromUri(URI.create(kakaoOauthConfig.getAuthorizationUri()))
                .queryParam("client_id", kakaoOauthConfig.getClientId())
                .queryParam("redirect_uri", kakaoOauthConfig.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("state", identifierProvider.generateIdentifier())
                .build()
                .toUriString();
    }

    @Override
    public OauthUser fetchUser(String code) {

        String token = getAccessToken(code);

        KakaoUserResponse user = oauthHttpClient.get(
                kakaoOauthConfig.getUserInfoUri(),
                h -> h.setBearerAuth(token),
                KakaoUserResponse.class
        );

        String email = (user.kakao_account() != null)
                ? user.kakao_account().email()
                : null;

        if (email == null) {
            email = "kakao_" + user.id() + "@noemail.local";
        }

        return new OauthUser(
                String.valueOf(user.id()),
                VerificationProvider.KAKAO,
                email
        );
    }

    private String getAccessToken(String code) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", kakaoOauthConfig.getClientId());
        form.add("client_secret", kakaoOauthConfig.getClientSecret());
        form.add("redirect_uri", kakaoOauthConfig.getRedirectUri());
        form.add("code", code);

        KakaoTokenResponse res = oauthHttpClient.postForm(
                kakaoOauthConfig.getTokenUri(),
                form,
                h -> {},
                KakaoTokenResponse.class
        );

        return res.access_token();
    }
}