package teamdevhub.devhub.api.project.model.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.project.domain.Project;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProjectBasicResponseDto {
	
	private String projectGuid;
    private String userGuid;
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
    
    private String registrantGuid;
    private LocalDateTime registeredDate;
    private String modifierGuid;
    private LocalDateTime modifiedDate;
    
    public static ProjectBasicResponseDto fromDomain(Project project) {
    	ProjectBasicResponseDtoBuilder<?, ?> builder = ProjectBasicResponseDto.builder();
    	fillBase(builder, project);
        return builder.build();
    }
        
    protected static <B extends ProjectBasicResponseDtoBuilder<?, ?>> void fillBase(B builder, Project project) {
        builder
	        .projectGuid(project.getProjectGuid())
	        .userGuid(project.getUserGuid())
	        .username(project.getUsername())
	        .category(project.getCategory())
	        .title(project.getTitle())
	        .content(project.getContent())
	        .recruitmentTypeCd(project.getRecruitmentTypeCd())
	        .recruitmentStartDate(project.getRecruitmentStartDate())
	        .recruitmentEndDate(project.getRecruitmentEndDate())
	        .progressTypeCd(project.getProgressTypeCd())
	        .progressRegionCd(project.getProgressRegionCd())
	        .progressPeriod(project.getProgressPeriod())
	        .progressStartDate(project.getProgressStartDate())
	        .progressEndDate(project.getProgressEndDate())
	        .registrantGuid(project.getAuditInfo().registrantGuid())
	        .registeredDate(project.getAuditInfo().registeredDate())
	        .modifierGuid(project.getAuditInfo().modifierGuid())
	        .modifiedDate(project.getAuditInfo().modifiedDate());
    }
    
}
