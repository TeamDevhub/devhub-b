package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;

import java.util.List;

public interface JpaUserPositionRepository extends JpaRepository<UserPositionEntity, String> {
    void deleteByUserGuid(String userGuid);
    List<UserPositionEntity> findByUserGuid(String userGuid);
}
