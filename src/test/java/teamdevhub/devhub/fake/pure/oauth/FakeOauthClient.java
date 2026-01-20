package teamdevhub.devhub.fake.pure.oauth;

import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.out.auth.OauthClient;

public class FakeOauthClient implements OauthClient {

    private final VerificationProvider supportedProvider;

    public FakeOauthClient(VerificationProvider supportedProvider) {
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
