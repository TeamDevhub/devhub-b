package teamdevhub.devhub.application.selector.oauth;

import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.out.oauth.OauthClient;

public interface OauthClientSelector {

     OauthClient select(VerificationProvider verificationProvider);
}
