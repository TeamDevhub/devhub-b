package teamdevhub.devhub.application.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.common.enums.TokenPrefix;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.auth.RefreshToken;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticatedUserUseCase;
import teamdevhub.devhub.port.in.user.usecase.UserLoginUseCase;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserResolver;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;
import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.port.out.provider.TokenParseProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final TokenParseProvider tokenParseProvider;
    private final AuthenticatedUserResolver authenticatedUserResolver;
    private final AuthenticatedUserUseCase authenticatedUserUseCase;
    private final UserLoginUseCase userLoginUseCase;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponseDto login(LoginCommand loginCommand) {
        AuthenticatedUser authenticatedUser = authenticatedUserResolver.getAuthenticatedUser(loginCommand.email(), loginCommand.password());
        IssuedToken token = issueLoginToken(authenticatedUser);
        return LoginResponseDto.ofEmailUser(token.prefix(), token.accessToken(), token.refreshToken());
    }

    @Override
    public LoginResponseDto loginWithOauth(AuthenticatedUser authenticatedUser) {
        IssuedToken token = issueLoginToken(authenticatedUser);
        return LoginResponseDto.existedOAuthUser(token.prefix(), token.accessToken(), token.refreshToken(), authenticatedUser.signupStatus());
    }

    @Override
    public TokenResponseDto reissueAccessToken(String token) {

        String userGuid = tokenParseProvider.getRefreshTokenInfo(token).userGuid();
        RefreshToken refreshToken = refreshTokenRepository.findByUserGuid(userGuid);

        if (refreshToken == null || !refreshToken.token().equals(token)) {
            throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        AuthenticatedUser authenticatedUser = authenticatedUserUseCase.getUserForReissue(userGuid);

        String newAccessToken = tokenIssueProvider.createAccessToken(
                authenticatedUser.userGuid(),
                authenticatedUser.signupStatus(),
                authenticatedUser.email(),
                authenticatedUser.userRole()
        );

        return TokenResponseDto.issue(newAccessToken);
    }

    @Override
    public void revoke(String userGuid) {
        refreshTokenRepository.deleteByUserGuid(userGuid);
    }

    private void issueRefreshToken(String userGuid, String token) {
        RefreshToken refreshToken = RefreshToken.of(userGuid, token);
        refreshTokenRepository.save(refreshToken);
    }

    private IssuedToken issueLoginToken(AuthenticatedUser authenticatedUser) {
        String prefix = TokenPrefix.BEARER.value();
        String accessToken = tokenIssueProvider.createAccessToken(
                authenticatedUser.userGuid(),
                authenticatedUser.signupStatus(),
                authenticatedUser.email(),
                authenticatedUser.userRole()
        );
        String refreshToken = tokenIssueProvider.createRefreshToken(authenticatedUser.userGuid());

        issueRefreshToken(authenticatedUser.userGuid(), refreshToken);
        userLoginUseCase.updateLastLoginDateTime(authenticatedUser.userGuid());

        return new IssuedToken(prefix, accessToken, refreshToken);
    }
}