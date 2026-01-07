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
import teamdevhub.devhub.common.security.UserAuthentication;
import teamdevhub.devhub.common.enums.ErrorCode;
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

        UserAuthentication userAuthentication = (UserAuthentication) authentication.getPrincipal();

        String prefix = tokenProvider.getPrefix();
        String accessToken = tokenProvider.createAccessToken(userAuthentication.getUser().userGuid(), userAuthentication.getUser().email(), userAuthentication.getUser().userRole());
        String refreshToken = tokenProvider.createRefreshToken(email);

        issueRefreshToken(email, refreshToken);
        userUseCase.updateLastLoginDateTime(userAuthentication.getUserGuid());

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
            throw BusinessRuleException.of(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        AuthenticatedUser authenticatedUser = userUseCase.getUserForAuth(email);

        String newAccessToken = tokenProvider.createAccessToken(
                authenticatedUser.userGuid(),
                authenticatedUser.email(),
                authenticatedUser.userRole()
        );

        return TokenResponseDto.issue(newAccessToken);
    }

    @Override
    public void revoke(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}