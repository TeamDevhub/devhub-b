package teamdevhub.devhub.core.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.auth.port.out.UserCredentialRepository;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
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
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final UserCredentialRepository userCredentialRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public String signupEmailUser(SignupUserCommand signupUserCommand) {
        return "";
    }

    @Override
    public UserCredential signupOAuthUser(OauthUser oauthUser) {
        return null;
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
