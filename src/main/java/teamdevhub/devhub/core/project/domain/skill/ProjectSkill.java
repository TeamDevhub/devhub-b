package teamdevhub.devhub.core.project.domain.skill;

import lombok.Builder;

@Builder
public record ProjectSkill(String projectSkillGuid, String projectGuid, String skillCd) {

}
