package teamdevhub.devhub.core.project.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;

@Getter
@Builder
public class Project {
	
	private final String projectGuid;
    private final String userGuid;
    private String username;
    private String category;
    private String title;
    private String content;
    private String recruitmentTypeCd;
    private LocalDateTime recruitmentStartDate;
    private LocalDateTime recruitmentEndDate;
    private String progressTypeCd;
    private String progressRegionCd;
    private String progressPeriod;
    private LocalDateTime progressStartDate;
    private LocalDateTime progressEndDate;
    private AuditInfo auditInfo;
    
}
