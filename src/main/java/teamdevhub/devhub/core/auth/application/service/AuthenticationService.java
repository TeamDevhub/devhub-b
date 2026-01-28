package teamdevhub.devhub.core.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.usecase.AuthenticationUseCase;
import teamdevhub.devhub.core.auth.port.out.token.RefreshTokenRepository;
import teamdevhub.devhub.core.auth.port.out.token.TokenIssueProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AuthResult login(AuthenticatedUser authenticatedUser) {
        String accessToken = tokenIssueProvider.createAccessToken(
                authenticatedUser.userGuid(),
                authenticatedUser.email(),
                authenticatedUser.userRole()
        );
        String refreshToken = tokenIssueProvider.createRefreshToken(authenticatedUser.userGuid());
        issueRefreshToken(authenticatedUser.userGuid(), refreshToken);
        return AuthResult.of(accessToken, refreshToken);
    }

    @Override
    public AuthResult reissueAccessToken(AuthenticatedUser authenticatedUser) {
        String newAccessToken = tokenIssueProvider.createAccessToken(
                authenticatedUser.userGuid(),
                authenticatedUser.email(),
                authenticatedUser.userRole()
        );
        return AuthResult.ofReissue(newAccessToken);
    }

    @Override
    public void revoke(String userGuid) {
        refreshTokenRepository.deleteByUserGuid(userGuid);
    }

    private void issueRefreshToken(String userGuid, String token) {
        RefreshToken refreshToken = RefreshToken.of(userGuid, token);
        refreshTokenRepository.save(refreshToken);
    }
}