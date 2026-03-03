package teamdevhub.devhub.core.application.port.in.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.application.model.request.CreateApplicationRequestDto;
import teamdevhub.devhub.api.application.model.response.ProjectApplicationAnswerDetailResponseDto;
import teamdevhub.devhub.api.application.model.response.ProjectApplicationBasicResponseDto;
import teamdevhub.devhub.api.application.model.response.ProjectApplicationDetailResponseDto;
import teamdevhub.devhub.api.application.model.response.ProjectApplicationDetailWrapperResponseDto;
import teamdevhub.devhub.api.application.model.response.ProjectApplicationListResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationUseCase;
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
	private final ProjectApplicationUseCase projectApplicationUseCase;
	private final ProjectUseCase projectUseCase;

	public DataApiResponseDto<Void> approveApplication(
		String applicationGuid,
		String approverGuid,
		boolean approved
	) {
		projectApplicationUseCase.approveApplication(
			ApproveApplicationCommand.builder()
				.applicationGuid(applicationGuid)
				.approverGuid(approverGuid)
				.approved(approved)
				.build()
		);
		return DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS);
	}

	public DataApiResponseDto<Void> createApplication(
		String projectGuid,
		String applicantGuid,
		CreateApplicationRequestDto requestDto
	) {
		projectApplicationUseCase.createApplication(requestDto.toCommand(projectGuid, applicantGuid));
		return DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS);
	}

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

	public DataApiResponseDto<ProjectApplicationDetailWrapperResponseDto> getApplicationDetail(
		String applicationGuid
	) {
		ProjectApplication application =
			projectApplicationQueryUseCase.getApplicationByGuid(applicationGuid);

		List<ProjectApplicationAnswer> answers =
			projectApplicationQueryUseCase.getAnswersByApplicationGuid(applicationGuid);

		List<ProjectApplicationAnswerDetailResponseDto> answerDtoList = answers.stream()
			.map(ProjectApplicationAnswerDetailResponseDto::fromDomain)
			.toList();

		ProjectApplicationDetailWrapperResponseDto responseDto = ProjectApplicationDetailWrapperResponseDto.builder()
			.projectApplicationBasicDto(ProjectApplicationBasicResponseDto.fromDomain(application))
			.projectApplicationAnswerList(answerDtoList)
			.build();

		return DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, responseDto);
	}
}
