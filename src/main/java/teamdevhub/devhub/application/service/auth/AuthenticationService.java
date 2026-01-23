package teamdevhub.devhub.application.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.domain.auth.RefreshToken;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.auth.usecase.AuthenticationUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.provider.TokenIssueProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponseDto login(AuthenticatedUser authenticatedUser) {
        IssuedToken issuedToken = issueLoginToken(authenticatedUser);
        return LoginResponseDto.of(issuedToken.accessToken(), issuedToken.refreshToken());
    }

    @Override
    public LoginResponseDto loginWithOauth(AuthenticatedUser authenticatedUser) {
        IssuedToken issuedToken = issueLoginToken(authenticatedUser);
        return LoginResponseDto.of(issuedToken.accessToken(), issuedToken.refreshToken());
    }

    @Override
    public TokenResponseDto reissueAccessToken(AuthenticatedUser authenticatedUser) {
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

    private IssuedToken issueLoginToken(AuthenticatedUser authenticatedUser) {
        String accessToken = tokenIssueProvider.createAccessToken(
                authenticatedUser.userGuid(),
                authenticatedUser.signupStatus(),
                authenticatedUser.email(),
                authenticatedUser.userRole()
        );
        String refreshToken = tokenIssueProvider.createRefreshToken(authenticatedUser.userGuid());
        issueRefreshToken(authenticatedUser.userGuid(), refreshToken);
        return new IssuedToken(accessToken, refreshToken);
    }

    private void issueRefreshToken(String userGuid, String token) {
        RefreshToken refreshToken = RefreshToken.of(userGuid, token);
        refreshTokenRepository.save(refreshToken);
    }
}