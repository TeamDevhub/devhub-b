package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * FakeUserCredentialRepository와 달리 findEmailCredentialByUserGuid 및 savePassword를 완전히 구현한 Fake.
 * 비밀번호 변경/초기화 테스트에서 사용한다.
 */
public class FakeFullUserCredentialRepository implements UserCredentialRepository {

    private final Map<String, AuthenticatedUser> byGuid = new HashMap<>();
    private final Map<String, AuthenticatedUser> byEmail = new HashMap<>();
    private final Map<String, AuthenticatedUser> byOAuth = new HashMap<>();
    private final Map<String, EmailUserCredential> emailCredentialByGuid = new HashMap<>();

    @Override
    public Optional<AuthenticatedUser> findUserCredentialByUserGuid(String userGuid) {
        return Optional.ofNullable(byGuid.get(userGuid));
    }

    @Override
    public Optional<AuthenticatedUser> findEmailUserCredentialByEmail(String email) {
        return Optional.ofNullable(byEmail.get(email));
    }

    @Override
    public Optional<AuthenticatedUser> findOAuthUserCredentialByOAuth(VerificationProvider verificationProvider, String oauthId) {
        String key = verificationProvider.name() + ":" + oauthId;
        return Optional.ofNullable(byOAuth.get(key));
    }

    @Override
    public void saveEmailUserCredential(AuthenticatedUser authenticatedUser, String encryptedPassword) {
        byGuid.put(authenticatedUser.userGuid(), authenticatedUser);
        byEmail.put(authenticatedUser.loginId(), authenticatedUser);
        EmailUserCredential credential = new EmailUserCredential(
                authenticatedUser.userGuid(),
                authenticatedUser.loginId(),
                encryptedPassword,
                UserRole.USER
        );
        emailCredentialByGuid.put(authenticatedUser.userGuid(), credential);
    }

    @Override
    public void saveOAuthUserCredential(AuthenticatedUser authenticatedUser, VerificationProvider verificationProvider, String oauthId) {
        String key = verificationProvider.name() + ":" + oauthId;
        byOAuth.put(key, authenticatedUser);
    }

    @Override
    public EmailUserCredential findEmailCredentialByUserGuid(String userGuid) {
        EmailUserCredential credential = emailCredentialByGuid.get(userGuid);
        if (credential == null) {
            throw AdapterDataException.of(ErrorCode.USER_NOT_FOUND);
        }
        return credential;
    }

    @Override
    public void savePassword(EmailUserCredential emailUserCredential) {
        emailCredentialByGuid.put(emailUserCredential.getUserGuid(), emailUserCredential);
    }
}
