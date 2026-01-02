package teamdevhub.devhub.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.domain.record.auth.RefreshToken;
import teamdevhub.devhub.port.in.auth.RefreshTokenUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void issueRefreshToken(String email, String token) {
        RefreshToken refreshToken = RefreshToken.of(email, token);
        refreshTokenRepository.save(refreshToken);
    }
}