package teamdevhub.devhub.adapter.out.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;

import java.util.List;
import java.util.Set;

public interface JpaUserSkillRepository  extends JpaRepository<UserSkillEntity, String> {
    List<UserSkillEntity> findByUserGuid(String userGuid);
    void deleteByUserGuidAndSkillCd(String s, String s1);
}
