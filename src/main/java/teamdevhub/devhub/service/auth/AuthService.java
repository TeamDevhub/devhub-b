package teamdevhub.devhub.service.auth;

import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.enums.JwtStatusEnum;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;
import teamdevhub.devhub.port.out.common.TokenProvider;
import teamdevhub.devhub.port.out.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements AuthUseCase {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

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
        User user = userRepository.findByEmail(email).orElseThrow(() -> AuthRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));
        refreshTokenRepository.findByEmail(email).filter(refreshToken -> refreshToken.token().equals(token)).orElseThrow(() -> AuthRuleException.of(ErrorCodeEnum.REFRESH_TOKEN_INVALID));
        String newAccessToken = tokenProvider.createAccessToken(user.getEmail(), user.getUserRole());
        return TokenResponseDto.reissue(newAccessToken);
    }

    @Override
    public void revoke(String email) {
        refreshTokenRepository.deleteByEmail(email);
    }
}