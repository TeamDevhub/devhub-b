package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.user.UserRole;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUserGuid(String userGuid);
    boolean existsByUserRole(UserRole role);
}