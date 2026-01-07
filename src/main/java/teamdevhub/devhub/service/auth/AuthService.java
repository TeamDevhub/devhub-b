package teamdevhub.devhub.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.adapter.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.out.auth.userDetail.LoginAuthentication;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.common.record.auth.RefreshToken;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.provider.TokenProvider;
import teamdevhub.devhub.service.common.exception.BusinessRuleException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements AuthUseCase {

    private final TokenProvider tokenProvider;
    private final UserUseCase userUseCase;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponseDto login(LoginCommand loginCommand) {
        String email = loginCommand.getEmail();
        String rawPassword = loginCommand.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, rawPassword)
        );

        LoginAuthentication loginAuthentication = (LoginAuthentication) authentication.getPrincipal();

        String prefix = tokenProvider.getPrefix();
        String accessToken = tokenProvider.createAccessToken(loginAuthentication.getUser().userGuid(), loginAuthentication.getUser().email(), loginAuthentication.getUser().userRole());
        String refreshToken = tokenProvider.createRefreshToken(email);

        issueRefreshToken(email, refreshToken);
        userUseCase.updateLastLoginDateTime(loginAuthentication.getUserGuid());

        return LoginResponseDto.of(prefix, accessToken, refreshToken);
    }

    @Override
    public void issueRefreshToken(String email, String token) {
        RefreshToken refreshToken = RefreshToken.of(email, token);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public TokenResponseDto reissueAccessToken(String token) {

        String email = tokenProvider.extractEmailFromRefreshToken(token);
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email);

        if (!refreshToken.token().equals(token)) {
            throw BusinessRuleException.of(ErrorCodeEnum.REFRESH_TOKEN_INVALID);
        }

        AuthenticatedUser user = userUseCase.getUserForAuth(email);

        String newAccessToken =
                tokenProvider.createAccessToken(
                        user.userGuid(),
                        user.email(),
                        user.userRole()
                );

        return TokenResponseDto.issue(newAccessToken);
    }

    @Override
    public void revoke(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}