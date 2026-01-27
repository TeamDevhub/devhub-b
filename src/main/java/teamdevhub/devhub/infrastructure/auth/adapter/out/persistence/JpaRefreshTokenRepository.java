package teamdevhub.devhub.infrastructure.auth.adapter.out.persistence;

import teamdevhub.devhub.infrastructure.auth.adapter.out.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByUserGuid(String userGuid);
    void deleteByUserGuid(String userGuid);
}