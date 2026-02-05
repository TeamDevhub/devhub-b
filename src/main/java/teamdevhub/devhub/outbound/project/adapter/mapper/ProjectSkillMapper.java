package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.project.domain.vo.skill.ProjectSkill;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectSkillEntity;

public class ProjectSkillMapper {

	public static ProjectSkillEntity toEntity(String projectSkillGuid, ProjectSkill projectSkill) {
		return ProjectSkillEntity.builder()
				.projectSkillGuid(projectSkillGuid)
				.projectGuid(projectSkill.projectGuid())
				.skillCd(projectSkill.skillCd())
				.build();
	}
}
