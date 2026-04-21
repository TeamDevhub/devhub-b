package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.Optional;

public interface UserCredentialRepository {

    Optional<UserCredential> findEmailUserCredentialByUserGuid(String userGuid);
    Optional<UserCredential> findEmailUserCredentialByEmail(String email);
    Optional<UserCredential> findOAuthUserCredentialByOAuth(VerificationProvider verificationProvider, String oauthId);
    void saveEmailUserCredential(UserCredential userCredential, String encryptedPassword);
    void saveOAuthUserCredential(UserCredential userCredential, VerificationProvider verificationProvider, String oauthId);
}
