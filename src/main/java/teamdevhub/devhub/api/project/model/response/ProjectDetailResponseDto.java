package teamdevhub.devhub.api.project.model.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.core.project.domain.ProjectDetail;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProjectDetailResponseDto extends ProjectBasicResponseDto {
	
	private List<String> skillList;
	private List<String> positionList;
	private String likeCount;
	
	public static ProjectDetailResponseDto fromDomain(ProjectDetail projectDetail) {
		ProjectDetailResponseDtoBuilder<?, ?> builder = ProjectDetailResponseDto.builder();
		fillBase(builder, projectDetail.getProject());
		return builder
				.build();
	}

}
