package teamdevhub.devhub.service.auth;

import teamdevhub.devhub.adapter.in.auth.dto.TokenResponseDto;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.out.auth.AuthPort;
import teamdevhub.devhub.port.out.common.AuthTokenPort;
import teamdevhub.devhub.port.out.user.UserPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements AuthUseCase {

    private final AuthTokenPort authTokenPort;
    private final AuthPort authPort;
    private final UserPort userPort;

    @Override
    public TokenResponseDto refreshAccessToken(String refreshToken) {

        JwtStatusEnum jwtStatusEnum = authTokenPort.validateToken(refreshToken);

        if (jwtStatusEnum == JwtStatusEnum.EXPIRED) {
            throw AuthRuleException.of(ErrorCodeEnum.TOKEN_EXPIRED);
        }

        if (jwtStatusEnum != JwtStatusEnum.VALID) {
            throw AuthRuleException.of(ErrorCodeEnum.REFRESH_TOKEN_INVALID);
        }

        String email = authTokenPort.getEmailFromRefreshToken(refreshToken);
        User user = userPort.findByEmail(email)
                .orElseThrow(() -> AuthRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));

        authPort.findByEmail(email).filter(token -> token.getRefreshToken().equals(refreshToken)).orElseThrow(() -> AuthRuleException.of(ErrorCodeEnum.REFRESH_TOKEN_INVALID));

        String newAccessToken = authTokenPort.createAccessToken(user.getEmail(), user.getRole());
        return TokenResponseDto.reissue(newAccessToken);
    }
}