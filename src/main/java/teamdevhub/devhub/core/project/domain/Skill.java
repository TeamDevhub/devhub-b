package teamdevhub.devhub.core.project.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectSkillCommand;

@Builder
@Getter
public class Skill {
	private String projectSkillGuid;
	private String projectGuid;
	private String skillCd;
	
	public static Skill createProjectSkill(String projectSkillGuid, CreateProjectSkillCommand createProjectSkillCommand) {
		return Skill.builder()
				.projectSkillGuid(projectSkillGuid)
				.projectGuid(createProjectSkillCommand.projectGuid())
				.skillCd(createProjectSkillCommand.skillCd())
				.build();
	}
}
