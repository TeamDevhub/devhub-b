package teamdevhub.devhub.fake.pure.oauth;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.domain.vo.user.OauthUser;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;

public class FakeOauthClient implements OauthClient {

    private OauthUser oauthUser;

    public void withOauthUser(OauthUser oauthUser) {
        this.oauthUser = oauthUser;
    }

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return true;
    }

    @Override
    public String getAuthorizationUrl() {
        return "https://oauth.test/authorize/";
    }

    @Override
    public OauthUser fetchUser(String authorizationCode) {
        return oauthUser;
    }
}

