package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;

import java.util.List;
import java.util.Set;

public interface JpaUserPositionRepository extends JpaRepository<UserPositionEntity, String> {
    List<UserPositionEntity> findByUserGuid(String userGuid);
    void deleteByUserGuidAndPositionCd(String userGuid, String positionCode);
}
