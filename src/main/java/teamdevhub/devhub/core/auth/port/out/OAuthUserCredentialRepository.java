package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.vo.user.OAuthUserCredential;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.Optional;

public interface OAuthUserCredentialRepository {

    Optional<OAuthUserCredential> findByProviderAndOauthId(VerificationProvider verificationProvider, String oauthId);
    Optional<OAuthUserCredential> findByUserGuid(String userGuid);
}
