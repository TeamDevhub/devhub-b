package teamdevhub.devhub.fake.pure.selector;

import teamdevhub.devhub.application.selector.oauth.OauthClientSelector;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.out.oauth.OauthClient;

public class FakeOauthClientSelector implements OauthClientSelector {

    private final OauthClient oauthClient;

    public FakeOauthClientSelector(OauthClient oauthClient) {
        this.oauthClient = oauthClient;
    }

    @Override
    public OauthClient select(VerificationProvider provider) {
        return oauthClient;
    }
}
