package teamdevhub.devhub.core.project.port.in.facade.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.project.domain.Project;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProjectDetailResponseDto extends ProjectBasicResponseDto {
	
	private List<String> skillList;
	private List<String> positionList;
	private String likeCount;
	
	public static ProjectDetailResponseDto fromDomain(Project project) {
		ProjectDetailResponseDtoBuilder<?, ?> builder = ProjectDetailResponseDto.builder();
		fillBase(builder, project);
		return builder
			.build();
	}

}
