package teamdevhub.devhub.core.project.domain;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Requirement {
	
	private String projectRequirementGuid;
    private String projectGuid;
    private String positionCd;
    private String levelCd;
    private int capacity;
    
}
