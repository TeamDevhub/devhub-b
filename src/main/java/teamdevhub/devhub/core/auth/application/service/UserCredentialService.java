package teamdevhub.devhub.core.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.auth.port.out.AuthenticatedUserResolver;
import teamdevhub.devhub.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.core.auth.port.out.password.EncodedPasswordProvider;
import teamdevhub.devhub.core.auth.port.out.token.RefreshTokenRepository;
import teamdevhub.devhub.core.auth.port.out.token.TokenParseProvider;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.core.user.port.in.command.UpdatePasswordCommand;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCredentialService implements UserCredentialUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final IdentifierProvider identifierProvider;
    private final EncodedPasswordProvider encodedPasswordProvider;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final UserCredentialRepository userCredentialRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String signupEmailUser(SignupUserCommand signupUserCommand) {
        userCredentialRepository.findEmailUserCredentialByEmail(signupUserCommand.email())
                .ifPresent(existingCredential -> {
                    throw BusinessRuleException.of(ErrorCode.SIGNUP_FAIL);
                });

        String userGuid = identifierProvider.generateIdentifier();
        String encryptedPassword = encodedPasswordProvider.encode(signupUserCommand.password());
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(userGuid, signupUserCommand.email(), UserRole.USER);

        userCredentialRepository.saveEmailUserCredential(authenticatedUser, encryptedPassword);
        return userGuid;
    }

    @Override
    public AuthenticatedUser signupOAuthUser(OauthUser oauthUser) {
        userCredentialRepository.findOAuthUserCredentialByOAuth(oauthUser.verificationProvider(), oauthUser.oauthId())
                .ifPresent(existingCredential -> {
                    throw BusinessRuleException.of(ErrorCode.SIGNUP_FAIL);
                });

        String userGuid = identifierProvider.generateIdentifier();
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(userGuid, oauthUser.oauthId(), UserRole.USER);

        userCredentialRepository.saveOAuthUserCredential(authenticatedUser, oauthUser.verificationProvider(), oauthUser.oauthId());
        return authenticatedUser;
    }

    @Override
    public AuthenticatedUser getUserForReissue(String refreshToken) {
        String userGuid = tokenParseProvider.getRefreshTokenInfo(refreshToken);
        RefreshToken savedRefreshToken = refreshTokenRepository.findByUserGuid(userGuid)
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID));

        if (!savedRefreshToken.token().equals(refreshToken)) {
            throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        return userCredentialRepository.findUserCredentialByUserGuid(userGuid)
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public AuthenticatedUser authenticate(LoginCommand loginCommand) {
        return authenticatedUserResolver.getAuthenticatedUser(loginCommand.email(), loginCommand.password());
    }

    @Override
    public void updatePassword(UpdatePasswordCommand updatePasswordCommand) {
        EmailUserCredential emailUserCredential = userCredentialRepository.findEmailCredentialByUserGuid(updatePasswordCommand.userGuid());
        emailUserCredential.verifyPassword(encodedPasswordProvider.matches(updatePasswordCommand.currentPassword(), emailUserCredential.getPassword()));
        emailUserCredential.changePassword(encodedPasswordProvider.encode(updatePasswordCommand.newPassword()));
        userCredentialRepository.savePassword(emailUserCredential);
    }

    @Override
    public void resetUserPassword(String userGuid, String newPassword) {
        EmailUserCredential emailUserCredential = userCredentialRepository.findEmailCredentialByUserGuid(userGuid);
        emailUserCredential.changePassword(encodedPasswordProvider.encode(newPassword));
        userCredentialRepository.savePassword(emailUserCredential);
    }

}
