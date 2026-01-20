package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.domain.user.vo.skill.UserSkill;

import java.util.Set;

public interface UserSkillRepository {

    void saveAll(Set<UserSkill> skills);
    Set<UserSkill> findByUserGuid(String userGuid);
    void replace(Set<UserSkill> previousSkills, Set<UserSkill> changedSkills);
}