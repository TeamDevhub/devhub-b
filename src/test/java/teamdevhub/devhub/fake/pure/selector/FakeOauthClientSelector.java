package teamdevhub.devhub.fake.pure.selector;

import teamdevhub.devhub.core.auth.application.selector.OauthClientSelector;
import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.port.out.OauthClient;

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
