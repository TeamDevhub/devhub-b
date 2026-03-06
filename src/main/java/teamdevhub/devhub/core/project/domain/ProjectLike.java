package teamdevhub.devhub.core.project.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProjectLike {
	private String projectLikeGuid;
	
    private String projectGuid;
	
    private String userGuid;
    
}
