package teamdevhub.devhub.adapter.out.user;

import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserGuid(String userGuid);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByUserRole(UserRole role);
}