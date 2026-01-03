package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;

import java.util.List;
import java.util.Set;

public interface JpaUserPositionRepository extends JpaRepository<UserPositionEntity, String> {
    void deleteByUserGuid(String userGuid);
    List<UserPositionEntity> findByUserGuid(String userGuid);

    @Query("select p.positionCd from UserPositionEntity p where p.userGuid = :userGuid")
    Set<String> findCodesByUserGuid(@Param("userGuid") String userGuid);

    void deleteByUserGuidAndPositionCdIn(String userGuid, Set<String> toDelete);
}
