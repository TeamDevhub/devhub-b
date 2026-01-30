package teamdevhub.devhub.core.project.domain.vo.skill;

import lombok.Builder;

@Builder
public record ProjectSkill(String projectSkillGuid, String projectGuid, String skillCd) {

}
