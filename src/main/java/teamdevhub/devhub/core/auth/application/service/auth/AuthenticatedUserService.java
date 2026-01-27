package teamdevhub.devhub.core.auth.application.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.shared.exception.BusinessRuleException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.domain.RefreshToken;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.core.auth.port.out.AuthenticatedUserResolver;
import teamdevhub.devhub.core.auth.port.out.RefreshTokenRepository;
import teamdevhub.devhub.core.common.provider.TokenParseProvider;
import teamdevhub.devhub.core.user.port.out.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticatedUserService implements AuthenticatedUserUseCase {

    private final TokenParseProvider tokenParseProvider;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthenticatedUser getUserForReissue(String refreshToken) {
        String userGuid = tokenParseProvider.getRefreshTokenInfo(refreshToken).userGuid();
        RefreshToken savedRefreshToken = refreshTokenRepository.findByUserGuid(userGuid);

        if (savedRefreshToken == null || !savedRefreshToken.token().equals(refreshToken)) {
            throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
        }
        return userRepository.findAuthenticatedUserByUserGuid(userGuid);
    }

    @Override
    public AuthenticatedUser authenticate(LoginCommand loginCommand) {
        return authenticatedUserResolver.getAuthenticatedUser(loginCommand.email(), loginCommand.password());
    }
}
