package teamdevhub.devhub.fake.pure.application.selector;

import teamdevhub.devhub.core.auth.application.selector.oauth.OauthClientSelector;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;

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
