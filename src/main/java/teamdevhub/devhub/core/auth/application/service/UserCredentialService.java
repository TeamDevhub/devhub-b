package teamdevhub.devhub.core.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.outbound.auth.infrastructure.oauth.OauthUser;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.out.AuthenticatedUserResolver;
import teamdevhub.devhub.core.auth.port.out.token.RefreshTokenRepository;
import teamdevhub.devhub.core.auth.port.out.token.TokenParseProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCredentialService implements UserCredentialUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final IdentifierProvider identifierProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final UserCredentialRepository userCredentialRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String signupEmailUser(SignupUserCommand signupUserCommand) {
        userCredentialRepository.findEmailUserCredentialByEmail(signupUserCommand.email())
                .ifPresent(c -> {
                    throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);
                });

        String userGuid = identifierProvider.generateIdentifier();
        String encryptedPassword = passwordEncoder.encode(signupUserCommand.password());
        UserCredential userCredential = UserCredential.of(
                userGuid,
                signupUserCommand.email(),
                UserRole.USER
        );

        userCredentialRepository.saveEmailUserCredential(
                userCredential,
                encryptedPassword
        );
        return userGuid;
    }

    @Override
    public UserCredential signupOAuthUser(OauthUser oauthUser) {
        userCredentialRepository
                .findOAuthUserCredentialByOAuth(
                        oauthUser.verificationProvider(),
                        oauthUser.oauthId()
                )
                .ifPresent(c -> {
                    throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);
                });
        String userGuid = identifierProvider.generateIdentifier();
        UserCredential userCredential = UserCredential.of(
                userGuid,
                oauthUser.email(),
                UserRole.USER
        );
        userCredentialRepository.saveOAuthUserCredential(
                userCredential,
                oauthUser.verificationProvider(),
                oauthUser.oauthId()
        );
        return userCredential;
    }

    @Override
    public UserCredential getUserForReissue(String refreshToken) {
        String userGuid = tokenParseProvider.getRefreshTokenInfo(refreshToken);
        RefreshToken savedRefreshToken = refreshTokenRepository.findByUserGuid(userGuid);

        if (savedRefreshToken == null || !savedRefreshToken.token().equals(refreshToken)) {
            throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
        }
        return userCredentialRepository.findEmailUserCredentialByUserGuid(userGuid).orElseThrow();
    }

    @Override
    public UserCredential authenticate(LoginCommand loginCommand) {
        return authenticatedUserResolver.getAuthenticatedUser(loginCommand.email(), loginCommand.password());
    }
}
