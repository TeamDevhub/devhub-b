package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.project.domain.vo.skill.ProjectSkill;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectSkillEntity;

public class ProjectSkillMapper {

	public static ProjectSkillEntity toEntity(ProjectSkill projectSkill) {
		return ProjectSkillEntity.builder()
				.projectSkillGuid(projectSkill.projectSkillGuid())
				.projectGuid(projectSkill.projectGuid())
				.skillCd(projectSkill.skillCd())
				.build();
	}
	
	public static ProjectSkill toProjectSkill(ProjectSkillEntity projectSkillEntity) {
		return ProjectSkill.builder()
				.projectSkillGuid(projectSkillEntity.getProjectSkillGuid())
				.projectGuid(projectSkillEntity.getProjectGuid())
				.skillCd(projectSkillEntity.getSkillCd())
				.build();
	}
}
