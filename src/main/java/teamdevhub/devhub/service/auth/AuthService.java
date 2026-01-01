package teamdevhub.devhub.service.auth;

import teamdevhub.devhub.adapter.in.auth.dto.TokenResponseDto;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenPort;
import teamdevhub.devhub.port.out.common.TokenProvider;
import teamdevhub.devhub.port.out.user.UserPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements AuthUseCase {

    private final TokenProvider tokenProvider;
    private final RefreshTokenPort refreshTokenPort;
    private final UserPort userPort;

    @Override
    public TokenResponseDto reissueAccessToken(String refreshToken) {

        JwtStatusEnum jwtStatusEnum = tokenProvider.validateToken(refreshToken);

        if (jwtStatusEnum == JwtStatusEnum.EXPIRED) {
            throw AuthRuleException.of(ErrorCodeEnum.TOKEN_EXPIRED);
        }

        if (jwtStatusEnum != JwtStatusEnum.VALID) {
            throw AuthRuleException.of(ErrorCodeEnum.REFRESH_TOKEN_INVALID);
        }

        String email = tokenProvider.getEmailFromRefreshToken(refreshToken);
        User user = userPort.findByEmail(email)
                .orElseThrow(() -> AuthRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));

        refreshTokenPort.findByEmail(email).filter(token -> token.getRefreshToken().equals(refreshToken)).orElseThrow(() -> AuthRuleException.of(ErrorCodeEnum.REFRESH_TOKEN_INVALID));

        String newAccessToken = tokenProvider.createAccessToken(user.getEmail(), user.getUserRole());
        return TokenResponseDto.reissue(newAccessToken);
    }

    @Override
    public void revoke(String email) {
        refreshTokenPort.deleteByEmail(email);
    }
}