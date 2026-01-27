package teamdevhub.devhub.core.auth.application.selector;

import teamdevhub.devhub.shared.enums.VerificationProvider;
import teamdevhub.devhub.core.auth.port.out.OauthClient;

public interface OauthClientSelector {

     OauthClient select(VerificationProvider verificationProvider);
}
