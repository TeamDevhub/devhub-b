package teamdevhub.devhub.service.authentication;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.port.in.authentication.AuthenticationUseCase;
import teamdevhub.devhub.port.in.authentication.command.LoginCommand;
import teamdevhub.devhub.adapter.in.dto.response.auth.LoginResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.auth.TokenResponseDto;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.user.vo.AuthenticatedUser;
import teamdevhub.devhub.domain.authentication.vo.RefreshToken;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.authentication.AuthenticatedUserProvider;
import teamdevhub.devhub.port.out.authentication.RefreshTokenRepository;
import teamdevhub.devhub.port.out.authentication.TokenIssueProvider;
import teamdevhub.devhub.service.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService implements AuthenticationUseCase {

    private final TokenIssueProvider tokenIssueProvider;
    private final AuthenticatedUserProvider authenticatedUserProvider;
    private final UserUseCase userUseCase;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponseDto login(LoginCommand loginCommand) {
        AuthenticatedUser authenticatedUser = authenticatedUserProvider.getAuthenticatedUser(loginCommand.email(), loginCommand.password());

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