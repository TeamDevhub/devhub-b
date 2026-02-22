package teamdevhub.devhub.api.application.model.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationListResponseDto {

	private ProjectDetailResponseDto projectDetailDto;
	private List<ProjectApplicationDetailResponseDto> applicationList;
	private PageResponseDto pagination;
}
