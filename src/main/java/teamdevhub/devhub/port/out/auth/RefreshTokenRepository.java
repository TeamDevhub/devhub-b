package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.domain.common.record.auth.RefreshToken;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    RefreshToken findByEmail(String email);
    void deleteByEmail(String email);
}