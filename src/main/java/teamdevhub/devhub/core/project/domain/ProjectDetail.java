package teamdevhub.devhub.core.project.domain;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectDetail {
	
	private Project project;
	private List<String> projectSkill;
	private List<Requirement> projectRequirement;
	private String likeCount;
   

	@Builder
    @QueryProjection
    public ProjectDetail(Project project, List<String> projectSkill, List<Requirement> projectRequirement, String likeCount) {
    	this.project = project;
    	this.projectSkill = projectSkill;
    	this.projectRequirement = projectRequirement;
    	this.likeCount = likeCount;
    }
    
}
