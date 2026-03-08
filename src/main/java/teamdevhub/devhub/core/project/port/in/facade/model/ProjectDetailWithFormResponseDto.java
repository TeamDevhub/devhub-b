package teamdevhub.devhub.core.project.port.in.facade.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.project.domain.Project;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProjectDetailWithFormResponseDto extends ProjectDetailResponseDto {
	
	private List<String> skillList;
	private List<RequirementResponseDto> positionList;
	private String likeCount;
	private boolean capacityClosed;
	private String recruitStatus;
	
	public static ProjectDetailResponseDto fromDomain(Project project) {
		ProjectDetailWithFormResponseDtoBuilder<?, ?> builder = ProjectDetailWithFormResponseDtoBuilder.builder();
		fillBase(builder, project);
		return builder
			.skillList(project.getProjectSkill())
			.positionList(project.getProjectRequirement().stream().map(RequirementResponseDto::fromDomain).toList())
			.likeCount(project.getLikeCount())
			.capacityClosed(project.isCapacityClosed())
			.recruitStatus(project.getRecruitStatus())
			.build();
	}


}