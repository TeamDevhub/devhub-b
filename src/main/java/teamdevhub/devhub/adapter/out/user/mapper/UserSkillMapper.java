package teamdevhub.devhub.adapter.out.user.mapper;

import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;

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
