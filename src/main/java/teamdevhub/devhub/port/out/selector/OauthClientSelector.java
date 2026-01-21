package teamdevhub.devhub.port.out.selector;

import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.out.oauth.OauthClient;

public interface OauthClientSelector {

     OauthClient select(VerificationProvider verificationProvider);
}
