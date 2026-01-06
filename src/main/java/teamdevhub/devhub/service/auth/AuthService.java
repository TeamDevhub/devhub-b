package teamdevhub.devhub.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.adapter.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.out.auth.userDetail.AuthenticatedUserDetails;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.adapter.in.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.common.record.auth.RefreshToken;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.provider.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        // UserDetails 가져오기
        AuthenticatedUserDetails userDetails = (AuthenticatedUserDetails) authentication.getPrincipal();
        AuthenticatedUser user = userDetails.getUser();

        // SecurityContext에 Principal을 AuthenticatedUser로 넣음
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                user,                          // Principal
                userDetails.getPassword(),      // Credentials
                userDetails.getAuthorities()    // 권한
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        String prefix = tokenProvider.getPrefix();
        String accessToken = tokenProvider.createAccessToken(email, userDetails.getUser().userRole());
        String refreshToken = tokenProvider.createRefreshToken(email);

        issueRefreshToken(email, refreshToken);
        userUseCase.updateLastLoginDateTime(userDetails.getUserGuid());

        return LoginResponseDto.of(prefix, accessToken, refreshToken);
    }

    @Override
    public void issueRefreshToken(String email, String token) {
        RefreshToken refreshToken = RefreshToken.of(email, token);
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public TokenResponseDto reissueAccessToken(String token) {

        JwtStatusEnum jwtStatusEnum = tokenProvider.validateToken(token);

        if (jwtStatusEnum == JwtStatusEnum.EXPIRED) {
            throw AuthRuleException.of(ErrorCodeEnum.TOKEN_EXPIRED);
        }

        if (jwtStatusEnum != JwtStatusEnum.VALID) {
            throw AuthRuleException.of(ErrorCodeEnum.REFRESH_TOKEN_INVALID);
        }

        String email = tokenProvider.getEmailFromRefreshToken(token);
        AuthenticatedUser authenticatedUser = userUseCase.getUserForAuth(email);
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email);

        if (!refreshToken.token().equals(token)) {
            throw AuthRuleException.of(ErrorCodeEnum.REFRESH_TOKEN_INVALID);
        }

        String newAccessToken = tokenProvider.createAccessToken(authenticatedUser.email(), authenticatedUser.userRole());
        return TokenResponseDto.issue(newAccessToken);
    }

    @Override
    public void revoke(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}