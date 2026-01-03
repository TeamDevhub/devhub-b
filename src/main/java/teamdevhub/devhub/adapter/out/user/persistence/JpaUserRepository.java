package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserGuid(String userGuid);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByUserRole(UserRole role);

    @Modifying
    @Query("UPDATE UserEntity u SET u.lastLoginDt = :lastLoginDate WHERE u.userGuid = :userGuid")
    void updateLastLoginDateTime(@Param("userGuid") String userGuid, @Param("lastLoginDate") LocalDateTime loginDate);
}