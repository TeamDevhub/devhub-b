package teamdevhub.devhub.adapter.out.infrastructure.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserGuid(String userGuid);
    Optional<UserEntity> findByProviderAndOauthId(VerificationProvider verificationProvider, String oauthId);

    @Modifying
    @Query("update UserEntity u set u.lastLoginDate = :lastLoginDate where u.userGuid = :userGuid")
    int updateLastLoginDateTime(@Param("userGuid") String userGuid, @Param("lastLoginDate") LocalDateTime lastLoginDate);

    boolean existsByUserRole(UserRole userRole);
}