package teamdevhub.devhub.adapter.out.auth;

import teamdevhub.devhub.adapter.out.auth.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByEmail(String email);
    void deleteByEmail(String email);
}