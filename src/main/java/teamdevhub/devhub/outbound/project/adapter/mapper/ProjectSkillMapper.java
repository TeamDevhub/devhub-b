package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.project.domain.Skill;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectSkillEntity;

public class ProjectSkillMapper {

	public static ProjectSkillEntity toEntity(Skill projectSkill) {
		return ProjectSkillEntity.builder()
				.projectSkillGuid(projectSkill.getProjectSkillGuid())
				.projectGuid(projectSkill.getProjectGuid())
				.skillCd(projectSkill.getSkillCd())
				.build();
	}
	
	public static Skill toProjectSkill(ProjectSkillEntity projectSkillEntity) {
		return Skill.builder()
				.projectSkillGuid(projectSkillEntity.getProjectSkillGuid())
				.projectGuid(projectSkillEntity.getProjectGuid())
				.skillCd(projectSkillEntity.getSkillCd())
				.build();
	}
}
