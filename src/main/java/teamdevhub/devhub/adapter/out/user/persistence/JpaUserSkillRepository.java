package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;

import java.util.List;

public interface JpaUserSkillRepository  extends JpaRepository<UserSkillEntity, String> {
    List<UserSkillEntity> findByUserGuid(String userGuid);
    void deleteByUserGuidAndSkillCd(String s, String s1);
}
