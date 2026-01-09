package teamdevhub.devhub.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.vo.auth.RefreshToken;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.auth.AuthenticatedUserProvider;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.auth.TokenIssueProvider;
import teamdevhub.devhub.service.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements AuthUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final UserUseCase userUseCase;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponseDto login(LoginCommand loginCommand) {
        AuthenticatedUser authenticatedUser = authenticatedUserProvider.getAuthenticatedUser(loginCommand.getEmail(), loginCommand.getPassword());

        String prefix = tokenIssueProvider.getPrefix();
        String accessToken = tokenIssueProvider.createAccessToken(authenticatedUser.userGuid(), authenticatedUser.email(), authenticatedUser.userRole());
        String refreshToken = tokenIssueProvider.createRefreshToken(authenticatedUser.userGuid());

        issueRefreshToken(authenticatedUser.userGuid(), refreshToken);
        userUseCase.updateLastLoginDateTime(authenticatedUser.userGuid());
        return LoginResponseDto.of(prefix, accessToken, refreshToken);
    }

    @Override
    public void issueRefreshToken(String email, String token) {
        RefreshToken refreshToken = RefreshToken.of(email, token);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public TokenResponseDto reissueAccessToken(String token) {

        String userGuid = tokenIssueProvider.extractUserGuidFromRefreshToken(token);
        RefreshToken refreshToken = refreshTokenRepository.findByUserGuid(userGuid);

        if (!refreshToken.token().equals(token)) {
            throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        AuthenticatedUser authenticatedUser = userUseCase.getUserForReissue(userGuid);

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
}