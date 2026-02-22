package teamdevhub.devhub.core.application.port.in.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.application.model.response.ProjectApplicationDetailResponseDto;
import teamdevhub.devhub.api.application.model.response.ProjectApplicationListResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.shared.enums.SuccessCode;

@Service
@RequiredArgsConstructor
public class ProjectApplicationFacade {

	private final ProjectApplicationQueryUseCase projectApplicationQueryUseCase;
	private final ProjectUseCase projectUseCase;

	public DataApiResponseDto<ProjectApplicationListResponseDto> getApplicationsByProjectGuid(
		String projectGuid,
		PageCommand pageCommand
	) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		ProjectDetailResponseDto projectDetailDto = ProjectDetailResponseDto.fromDomain(project);

		PageResult<ProjectApplication> pagedApplications =
			projectApplicationQueryUseCase.getApplicationsByProjectGuid(projectGuid, pageCommand);

		List<ProjectApplicationDetailResponseDto> applicationList = pagedApplications.content().stream()
			.map(ProjectApplicationDetailResponseDto::fromDomain)
			.toList();

		ProjectApplicationListResponseDto responseDto = ProjectApplicationListResponseDto.builder()
			.projectDetailDto(projectDetailDto)
			.applicationList(applicationList)
			.pagination(PageResponseDto.from(pagedApplications))
			.build();

		return DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, responseDto);
	}
}
