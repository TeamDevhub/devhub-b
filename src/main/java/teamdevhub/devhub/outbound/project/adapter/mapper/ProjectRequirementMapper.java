package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.project.domain.vo.requirement.ProjectRequirement;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;

public class ProjectRequirementMapper {
	
	public static ProjectRequirementEntity toEntity(String projectRequirementGuid, ProjectRequirement projectRequirement) {
		return ProjectRequirementEntity.builder()
				.projectRequirementGuid(projectRequirementGuid)
				.projectGuid(projectRequirement.projectGuid())
				.positionCd(projectRequirement.position())
				.levelCd(projectRequirement.level())
				.capacity(projectRequirement.capacity())
				.build();
	}
}
