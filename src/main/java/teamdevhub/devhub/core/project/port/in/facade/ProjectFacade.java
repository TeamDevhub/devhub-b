package teamdevhub.devhub.core.project.port.in.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectQueryUseCase;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;
import teamdevhub.devhub.shared.enums.SuccessCode;

@Service
@RequiredArgsConstructor
public class ProjectFacade {
	
	private final ProjectQueryUseCase projectQueryUseCase;
	private final ProjectUseCase projectUseCase;
	private final ApplicationFormUseCase applicationFormUseCase;

	public DataListApiResponseDto<ProjectDetailResponseDto> getProjectList(SearchProjectListCommand projectListSearchRequestDto, PageCommand pageCommand) {
		
		PageResult<Project> pagedProjectList = projectQueryUseCase.getProjectList(projectListSearchRequestDto, pageCommand);
        List<ProjectDetailResponseDto> projectDetailResponseDtoList = pagedProjectList.content().stream()
                .map(ProjectDetailResponseDto::fromDomain)
                .toList();
		
		return DataListApiResponseDto.successWithDataList(
                SuccessCode.READ_SUCCESS,
                projectDetailResponseDtoList,
                PageResponseDto.from(pagedProjectList)
		);
	}
	
	public void createProject(CreateProjectCommand createProjectCommand) {
		List<ApplicationFormEntity> additionalFormEntityList = applicationFormUseCase.saveApplicationForms(createProjectCommand.additionalFormList());
		List<String> additionalFormGuidList = additionalFormEntityList.stream()
				.map(additionalFormEntity -> additionalFormEntity.getApplicationFormGuid())
				.toList();
		createProjectCommand.applicationFormList().addAll(additionalFormGuidList);
		projectUseCase.createProject(createProjectCommand);
	}

}
