package teamdevhub.devhub.service.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.port.in.auth.RefreshTokenUseCase;
import teamdevhub.devhub.port.out.auth.RefreshTokenPort;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenPort refreshTokenPort;

    @Override
    public void issueRefreshToken(String email, String refreshToken) {
        refreshTokenPort.findByEmail(email).ifPresentOrElse(
                entity -> entity.rotate(refreshToken),
                () -> refreshTokenPort.save(email, refreshToken));
    }
}