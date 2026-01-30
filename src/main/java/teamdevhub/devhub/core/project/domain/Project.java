package teamdevhub.devhub.core.project.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.project.domain.projectRequirement.ProjectRequirement;
import teamdevhub.devhub.core.project.domain.skill.ProjectSkill;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectCommand;

@Getter
public class Project {
	
	private final String projectGuid;
	private String attachmentFileGuid;
	private String imageFileGuid;
	private final String userGuid;
	private String recruitmentTypeCd;
	private String progressTypeCd;
	private String progressRegionCd;
	private final String username;
	private String category;
	private String title;
	private String content;
	private LocalDateTime recuritmentStartDate;
	private LocalDateTime recuritmentEndDate;
	private LocalDateTime progressStartDate;
	private LocalDateTime progressEndDate;
	private Set<ProjectSkill> skillList;
	private Set<ProjectRequirement> positionList;
	private boolean deleted;
	private boolean capacityClosed;
	
	@Builder
	private Project(
			String projectGuid,
			String attachmentFileGuid,
			String imageFileGuid,
			String userGuid,
			String recruitmentTypeCd,
			String progressTypeCd,
			String progressRegionCd,
			String username,
			String category,
			String title,
			String content,
			LocalDateTime recuritmentStartDate,
			LocalDateTime recuritmentEndDate,
			LocalDateTime progressStartDate,
			LocalDateTime progressEndDate,
			Set<ProjectSkill> skillList,
			Set<ProjectRequirement> positionList,
			boolean deleted,
			boolean capacityClosed
	) {
		this.projectGuid = projectGuid;
		this.attachmentFileGuid = attachmentFileGuid;
		this.imageFileGuid = imageFileGuid;
		this.userGuid = userGuid;
		this.recruitmentTypeCd =recruitmentTypeCd;
		this.progressTypeCd = progressTypeCd;
		this.progressRegionCd = progressRegionCd;
		this.username = username;
		this.category = category;
		this.title = title;
		this.content = content;
		this.recuritmentStartDate = recuritmentStartDate;
		this.recuritmentEndDate = recuritmentEndDate;
		this.progressStartDate = progressStartDate;
		this.progressEndDate = progressEndDate;
		this.skillList = Objects.requireNonNullElseGet(skillList, HashSet::new);
		this.positionList = Objects.requireNonNullElseGet(positionList, HashSet::new);
		this.deleted = deleted;
		this.capacityClosed = capacityClosed;
	}
	
	public static Project createProject(CreateProjectCommand createProjectCommand, String projectGuid, String username) {
		return Project.builder()
				.projectGuid(projectGuid)
				.userGuid(createProjectCommand.userGuid())
				.recruitmentTypeCd(createProjectCommand.recuritmentTypeCd())
				.progressTypeCd(createProjectCommand.progressTypeCd())
				.progressRegionCd(createProjectCommand.progressRegionCd())
				.username(username)
				.category(createProjectCommand.category())
				.title(createProjectCommand.title())
				.content(createProjectCommand.content())
				.recuritmentStartDate(createProjectCommand.recuritmentStartDate())
				.recuritmentEndDate(createProjectCommand.recuritmentEndDate())
				.progressStartDate(createProjectCommand.progressStartDate())
				.progressEndDate(createProjectCommand.progressEndDate())
				.deleted(false)
				.capacityClosed(false)
				.build();
	}
	
	
}
