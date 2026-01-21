package teamdevhub.devhub.adapter.out.infrastructure.oauth;

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

    /**
     * 해당 Oauth 로그인이 가능한지 응답해주는 OauthClient 공통 메서드
     * @param verificationProvider 외부 인증 주체(ex 구글, 깃헙, 카카오)
     * @return 사용가능 여부에 따라 true/false 반환
     */
    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return verificationProvider == VerificationProvider.GOOGLE;
    }

    /**
     * 현재 작성된 소스 점검 필요
     * @return 구글 로그인 페이지로 이동하는 리다이렉트 URL
     */
    @Override
    public String getAuthorizationUrl() {
        return new GoogleAuthorizationCodeRequestUrl(clientId, redirectUri, List.of("profile", "email"))
                .setState(identifierProvider.generateIdentifier())
                .build();
    }

    /**
     * 인증된 사용자를 가져오는 함수, 추후 개발 필요
     * @param code 인증 코드
     * @return 외부 인증 주체가 제공하는 인증된 사용자
     */
    @Override
    public OauthUser fetchUser(String code) {
        String value = clientSecret;
        return new OauthUser("google-id", VerificationProvider.GOOGLE,  "test@gmail.com");
    }
}
