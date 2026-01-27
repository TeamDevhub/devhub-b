package teamdevhub.devhub.infrastructure.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.infrastructure.user.adapter.out.entity.UserPositionEntity;

import java.util.List;
import java.util.Set;

public interface JpaUserPositionRepository extends JpaRepository<UserPositionEntity, String> {

    List<UserPositionEntity> findByUserGuid(String userGuid);
    void deleteByUserGuidAndPositionCdIn(String userGuid, Set<String> positionCds);
}
