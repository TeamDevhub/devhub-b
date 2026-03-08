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
import teamdevhub.devhub.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.usecase.UserProfileUseCase;
import teamdevhub.devhub.shared.enums.SuccessCode;

@Service
@RequiredArgsConstructor
public class ProjectFacade {
	
	private final ProjectUseCase projectUseCase;
	private final ApplicationFormUseCase applicationFormUseCase;
	private final UserProfileUseCase userProfileUseCase;

	public DataListApiResponseDto<ProjectDetailResponseDto> getProjectList(SearchProjectListCommand projectListSearchRequestCommand, PageCommand pageCommand) {
		
		PageResult<Project> pagedProjectList = projectUseCase.getProjectList(projectListSearchRequestCommand, pageCommand);
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
		User user = userProfileUseCase.getUserInfo(createProjectCommand.userGuid());
		List<String> additionalFormGuidList = applicationFormUseCase.saveApplicationForms(createProjectCommand.additionalFormList());
		createProjectCommand.applicationFormList().addAll(additionalFormGuidList);
		projectUseCase.createProject(createProjectCommand, user.getUsername());
	}

	public ProjectDetailResponseDto getProjectDetail(String projectGuid) {
        Project project = projectUseCase.getProjectDetail(projectGuid);
        return ProjectDetailResponseDto.fromDomain(project);
	}

	public void deleteProjectDetail(String projectGuid) {
		// 프로젝트 지원자 조회 후 지원자가 있으면 return, 지원자 없으면 continue??
		// 삭제해야할 신청폼 목록 반환?
		List<String> deleteApplicationFormGuids =  projectUseCase.deleteProject(projectGuid);
		applicationFormUseCase.deleteApplicationForms(deleteApplicationFormGuids);
		
	}

	public void updateProject(String projectGuid, UpdateProjectCommand updateProjectCommand) {
		List<String> additionalFormGuidList = applicationFormUseCase.saveApplicationForms(updateProjectCommand.additionalFormList());
		updateProjectCommand.applicationFormList().addAll(additionalFormGuidList);
		List<String> deleteApplicationFormGuids = projectUseCase.updateProject(projectGuid, updateProjectCommand);
		applicationFormUseCase.deleteApplicationForms(deleteApplicationFormGuids);
	}

}
