package teamdevhub.devhub.application.oauth;

import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.port.out.auth.OauthClient;

public interface OauthProviderSelector {

     OauthClient select(VerificationProvider verificationProvider);
}
