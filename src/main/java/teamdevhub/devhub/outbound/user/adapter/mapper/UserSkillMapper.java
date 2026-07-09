package teamdevhub.devhub.outbound.user.adapter.mapper;

import teamdevhub.devhub.outbound.user.adapter.entity.UserSkillEntity;
import teamdevhub.devhub.core.user.domain.vo.skill.UserSkill;

public class UserSkillMapper {

    public static UserSkillEntity toEntity(String userSkillGuid, UserSkill userSkill) {
        return UserSkillEntity.builder()
                .userSkillGuid(userSkillGuid)
                .userGuid(userSkill.userGuid())
                .skillCd(userSkill.skillCd())
                .build();
    }

    public static UserSkill toRecord(UserSkillEntity userSkillEntity) {
        return new UserSkill(
                userSkillEntity.getUserGuid(),
                userSkillEntity.getSkillCd()
        );
    }
}
