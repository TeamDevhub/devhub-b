package teamdevhub.devhub.core.project.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.shared.enums.ProjectRecruitStatus;

@Getter
@Builder
public class Project {

	private final String projectGuid;

    private final String userGuid;
    private String username;
    private String category;
    private String title;
    private String content;
    private String attachmentFileGuid;
	private String imageFileGuid;

    private String recruitmentTypeCd;
    private LocalDate recruitmentStartDate;
    private LocalDate recruitmentEndDate;

    private String progressTypeCd;
    private String progressRegionCd;
    private String progressPeriod;
    private LocalDate progressStartDate;
    private LocalDate progressEndDate;

    private boolean deleted;
    private boolean capacityClosed;

	private List<String> projectSkill;
	private List<ProjectRequirement> projectRequirement;
	private String likeCount;
	private String recruitStatus;

	private AuditInfo auditInfo;
	
	public static Project createProject(CreateProjectCommand createProjectCommand, String projectGuid, String username) {
		return Project.builder()
				.projectGuid(projectGuid)
				.attachmentFileGuid(createProjectCommand.attachmentFileGuid())
				.imageFileGuid(createProjectCommand.imageFileGuid())
				.userGuid(createProjectCommand.userGuid())
				.recruitmentTypeCd(createProjectCommand.recruitmentTypeCd())
				.progressTypeCd(createProjectCommand.progressTypeCd())
				.progressRegionCd(createProjectCommand.progressRegionCd())
				.username(username)
				.category(createProjectCommand.category())
				.title(createProjectCommand.title())
				.content(createProjectCommand.content())
				.recruitmentStartDate(createProjectCommand.recruitmentStartDate())
				.recruitmentEndDate(createProjectCommand.recruitmentEndDate())
				.progressStartDate(createProjectCommand.progressStartDate())
				.progressEndDate(createProjectCommand.progressEndDate())
				.deleted(false)
				.capacityClosed(false)
				.build();
	}
	
	public static Project createUpateProject(UpdateProjectCommand updateProjectCommand, String projectGuid) {
		return Project.builder()
				.projectGuid(projectGuid)
				.attachmentFileGuid(updateProjectCommand.attachmentFileGuid())
				.imageFileGuid(updateProjectCommand.imageFileGuid())
				.userGuid(updateProjectCommand.userGuid())
				.recruitmentTypeCd(updateProjectCommand.recruitmentTypeCd())
				.progressTypeCd(updateProjectCommand.progressTypeCd())
				.progressRegionCd(updateProjectCommand.progressRegionCd())
				.username(updateProjectCommand.username())
				.category(updateProjectCommand.category())
				.title(updateProjectCommand.title())
				.content(updateProjectCommand.content())
				.recruitmentStartDate(updateProjectCommand.recruitmentStartDate())
				.recruitmentEndDate(updateProjectCommand.recruitmentEndDate())
				.progressStartDate(updateProjectCommand.progressStartDate())
				.progressEndDate(updateProjectCommand.progressEndDate())
				.deleted(false)
				.capacityClosed(false)
				.build();
	}
	
	public String getRecruitStatus() {
		LocalDate now = LocalDate.now();
		
		if (now.isBefore(this.recruitmentStartDate)) {
	        return ProjectRecruitStatus.WAITING.getCode();
	    }
	    if (now.isAfter(this.recruitmentEndDate)) {
	        return ProjectRecruitStatus.COMPLETED.getCode();
	    }
	    return ProjectRecruitStatus.RECRUITING.getCode();
	}
}
