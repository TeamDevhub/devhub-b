package teamdevhub.devhub.core.project.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementCommand;

@Builder
@Getter
public class Requirement {
	
	private String projectRequirementGuid;
	
	private String projectGuid;
	
	private String positionCd;
	
	private String levelCd;
	
	private int capacity;
	
	public static Requirement createProjectRequirement(String projectRequirementGuid, CreateProjectRequirementCommand createProjectRequirementCommand) {
		return Requirement.builder()
				.projectRequirementGuid(projectRequirementGuid)
				.projectGuid(createProjectRequirementCommand.projectGuid())
				.positionCd(createProjectRequirementCommand.position())
				.levelCd(createProjectRequirementCommand.level())
				.capacity(createProjectRequirementCommand.capacity())
				.build();
	}
	
}
