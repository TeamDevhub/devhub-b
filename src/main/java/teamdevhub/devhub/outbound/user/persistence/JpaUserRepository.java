package teamdevhub.devhub.outbound.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUserGuid(String userGuid);
    @Modifying
    @Query("update UserEntity u set u.lastLoginDate = :lastLoginDate where u.userGuid = :userGuid")
    int updateLastLoginDateTime(@Param("userGuid") String userGuid, @Param("lastLoginDate") LocalDateTime lastLoginDate);

    boolean existsByUserRole(UserRole userRole);
    
    @Query("select u.userGuid, u.username from UserEntity u where u.userGuid IN (:userGuids)")
    List<Object[]> findNamesByUserGuid(@Param("userGuids") List<String> userGuids);
}