package teamdevhub.devhub.core.project.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.core.common.audit.AuditInfo;

@Getter
@Builder
public class Requirement {
	
	private String projectRequirementGuid;
    private String projectGuid;
    private String positionCd;
    private String levelCd;
    private int capacity;
    
}
