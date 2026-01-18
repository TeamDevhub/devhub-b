package teamdevhub.devhub.adapter.out.infrastructure.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;

import java.util.List;
import java.util.Set;

public interface JpaUserPositionRepository extends JpaRepository<UserPositionEntity, String> {

    List<UserPositionEntity> findByUserGuid(String userGuid);
    void deleteByUserGuidAndPositionCdIn(String userGuid, Set<String> positionCds);
}
