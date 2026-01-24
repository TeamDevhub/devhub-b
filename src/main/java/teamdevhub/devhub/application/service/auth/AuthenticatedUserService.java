package teamdevhub.devhub.application.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.auth.RefreshToken;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserResolver;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;
import teamdevhub.devhub.port.out.user.UserRepository;

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
