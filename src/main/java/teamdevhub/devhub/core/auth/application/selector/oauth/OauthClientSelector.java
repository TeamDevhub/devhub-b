package teamdevhub.devhub.core.auth.application.selector.oauth;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.port.out.oauth.OauthClient;

public interface OauthClientSelector {

     OauthClient select(VerificationProvider verificationProvider);
}
