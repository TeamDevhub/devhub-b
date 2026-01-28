package teamdevhub.devhub.fake.pure.oauth;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.user.OauthUser;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;

public class StubOauthClient implements OauthClient {

    private final VerificationProvider supportedProvider;

    public StubOauthClient(VerificationProvider supportedProvider) {
        this.supportedProvider = supportedProvider;
    }

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return supportedProvider == verificationProvider;
    }

    @Override
    public String getAuthorizationUrl() {
        return "https://fake-oauth.com/auth";
    }

    @Override
    public OauthUser fetchUser(String authorizationCode) {
        throw new UnsupportedOperationException("selector 테스트 범위 아님");
    }
}
