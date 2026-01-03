package teamdevhub.devhub.service.auth;

import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.record.auth.AuthUser;
import teamdevhub.devhub.domain.record.auth.RefreshToken;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.common.TokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements AuthUseCase {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserUseCase userUseCase;

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
        AuthUser authUser = userUseCase.getAuthUser(email);
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email);
        if (!refreshToken.token().equals(token)) {
            throw AuthRuleException.of(ErrorCodeEnum.REFRESH_TOKEN_INVALID);
        }
        String newAccessToken = tokenProvider.createAccessToken(authUser.email(), authUser.userRole());
        return TokenResponseDto.reissue(newAccessToken);
    }

    @Override
    public void revoke(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}