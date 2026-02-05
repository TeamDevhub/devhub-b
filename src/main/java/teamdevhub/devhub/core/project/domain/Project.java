package teamdevhub.devhub.core.project.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;

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
    private LocalDateTime recruitmentStartDate;
    private LocalDateTime recruitmentEndDate;
    private String progressTypeCd;
    private String progressRegionCd;
    private String progressPeriod;
    private LocalDateTime progressStartDate;
    private LocalDateTime  progressEndDate;
    private AuditInfo auditInfo;
    private boolean deleted;
    private boolean capacityClosed;
	
	public static Project createProject(CreateProjectCommand createProjectCommand, String projectGuid) {
		return Project.builder()
				.projectGuid(projectGuid)
				.attachmentFileGuid(createProjectCommand.attachmentFileGuid())
				.imageFileGuid(createProjectCommand.imageFileGuid())
				.userGuid(createProjectCommand.userGuid())
				.recruitmentTypeCd(createProjectCommand.recruitmentTypeCd())
				.progressTypeCd(createProjectCommand.progressTypeCd())
				.progressRegionCd(createProjectCommand.progressRegionCd())
				.username(createProjectCommand.username())
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
    
}
