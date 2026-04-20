package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.vo.OAuthCredential;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.Optional;

public interface OauthCredentialRepository {

    Optional<OAuthCredential> findByProviderAndOauthId(VerificationProvider verificationProvider, String oauthId);
    Optional<OAuthCredential> findByUserGuid(String userGuid);
}
