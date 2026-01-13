package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;

import java.util.List;
import java.util.Set;

public interface JpaUserSkillRepository  extends JpaRepository<UserSkillEntity, String> {
    List<UserSkillEntity> findByUserGuid(String userGuid);
    void deleteByUserGuidAndSkillCdIn(String userGuid, Set<String> skillCds);
}
