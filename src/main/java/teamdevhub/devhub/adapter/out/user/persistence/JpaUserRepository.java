package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserGuid(String userGuid);

    @Modifying
    @Query("UPDATE UserEntity u SET u.lastLoginDt = :lastLoginDateTime WHERE u.userGuid = :userGuid")
    void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime);

    boolean existsByUserRole(UserRole role);
}