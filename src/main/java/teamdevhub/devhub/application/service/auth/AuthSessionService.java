package teamdevhub.devhub.application.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.port.in.auth.AuthSessionUseCase;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.dto.response.auth.LoginResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.auth.TokenResponseDto;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;
import teamdevhub.devhub.domain.auth.vo.RefreshToken;
import teamdevhub.devhub.port.in.auth.AuthUserUseCase;
import teamdevhub.devhub.port.in.user.usecase.UserLoginUseCase;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserResolver;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;
import teamdevhub.devhub.application.exception.BusinessRuleException;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthSessionService implements AuthSessionUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final AuthUserUseCase authUserUseCase;
    private final UserLoginUseCase userLoginUseCase;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponseDto login(LoginCommand loginCommand) {
        AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser(loginCommand.email(), loginCommand.password());

        String prefix = tokenIssueProvider.getPrefix();
        String accessToken = tokenIssueProvider.createAccessToken(authenticatedUser.userGuid(), authenticatedUser.email(), authenticatedUser.userRole());
        String refreshToken = tokenIssueProvider.createRefreshToken(authenticatedUser.userGuid());

        issueRefreshToken(authenticatedUser.userGuid(), refreshToken);
        userLoginUseCase.updateLastLoginDateTime(authenticatedUser.userGuid());
        return LoginResponseDto.of(prefix, accessToken, refreshToken);
    }

    @Override
    public TokenResponseDto reissueAccessToken(String token) {

        String userGuid = tokenIssueProvider.extractUserGuidFromRefreshToken(token);
        RefreshToken refreshToken = refreshTokenRepository.findByUserGuid(userGuid);

        if (refreshToken == null || !refreshToken.token().equals(token)) {
            throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        AuthenticatedUser authenticatedUser = authUserUseCase.getUserForReissue(userGuid);

        String newAccessToken = tokenIssueProvider.createAccessToken(
                authenticatedUser.userGuid(),
                authenticatedUser.email(),
                authenticatedUser.userRole()
        );

        return TokenResponseDto.issue(newAccessToken);
    }

    @Override
    public void revoke(String userGuid) {
        refreshTokenRepository.deleteByUserGuid(userGuid);
    }

    private void issueRefreshToken(String email, String token) {
        RefreshToken refreshToken = RefreshToken.of(email, token);
        refreshTokenRepository.save(refreshToken);
    }
}