package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.domain.user.vo.UserSkill;

import java.util.Set;

public interface UserSkillRepository {
    Set<UserSkill> findByUserGuid(String userGuid);
}
