package teamdevhub.devhub.infrastructure.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.infrastructure.user.adapter.out.entity.UserSkillEntity;

import java.util.List;
import java.util.Set;

public interface JpaUserSkillRepository  extends JpaRepository<UserSkillEntity, String> {

    List<UserSkillEntity> findByUserGuid(String userGuid);
    void deleteByUserGuidAndSkillCdIn(String userGuid, Set<String> skillCds);
}
