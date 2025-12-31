package teamdevhub.devhub.service.auth;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import teamdevhub.devhub.port.out.auth.RefreshTokenPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenPort refreshTokenPort;

    public void save(String email, String refreshToken) {
        refreshTokenPort.findByEmail(email).ifPresentOrElse(
                entity -> entity.rotate(refreshToken),
                () -> refreshTokenPort.save(email, refreshToken));
    }

    public Optional<RefreshTokenEntity> findByEmail(String email) {
        return refreshTokenPort.findByEmail(email);
    }

    public void delete(String email) {
        refreshTokenPort.deleteByEmail(email);
    }
}