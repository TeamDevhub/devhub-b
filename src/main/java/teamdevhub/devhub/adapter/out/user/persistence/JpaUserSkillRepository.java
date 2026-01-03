package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;

import java.util.List;

public interface JpaUserSkillRepository  extends JpaRepository<UserSkillEntity, String> {
    void deleteByUserGuid(String userGuid);
    List<UserSkillEntity> findByUserGuid(String userGuid);
}
