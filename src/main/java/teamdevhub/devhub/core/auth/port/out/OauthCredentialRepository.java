package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.vo.OAuthCredential;
import teamdevhub.devhub.shared.enums.VerificationProvider;

public interface OauthCredentialRepository {

    OAuthCredential findByProviderAndOauthId(VerificationProvider verificationProvider, String oauthId);
    OAuthCredential findByUserGuid(String userGuid);
}
