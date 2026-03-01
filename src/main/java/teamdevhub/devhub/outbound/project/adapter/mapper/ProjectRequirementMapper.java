package teamdevhub.devhub.outbound.project.adapter.mapper;

import teamdevhub.devhub.core.project.domain.Requirement;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;

public class ProjectRequirementMapper {
	
	public static ProjectRequirementEntity toEntity(Requirement projectRequirement) {
		return ProjectRequirementEntity.builder()
				.projectRequirementGuid(projectRequirement.getProjectRequirementGuid())
				.projectGuid(projectRequirement.getProjectGuid())
				.positionCd(projectRequirement.getPositionCd())
				.levelCd(projectRequirement.getLevelCd())
				.capacity(projectRequirement.getCapacity())
				.build();
	}
	
	public static Requirement toProjectRequirement(ProjectRequirementEntity projectRequirementEntity) {
		return Requirement.builder()
				.projectRequirementGuid(projectRequirementEntity.getProjectRequirementGuid())
				.projectGuid(projectRequirementEntity.getProjectGuid())
				.positionCd(projectRequirementEntity.getPositionCd())
				.levelCd(projectRequirementEntity.getLevelCd())
				.capacity(projectRequirementEntity.getCapacity())
				.build();
				
	}
}
