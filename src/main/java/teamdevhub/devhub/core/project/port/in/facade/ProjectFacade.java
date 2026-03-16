package teamdevhub.devhub.core.project.port.in.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.in.command.ApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.facade.model.ApplicationFormResponseDto;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.file.port.in.usecase.FileUseCase;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailWithFormResponseDto;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectApplicationFormUseCase;
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
	private final ProjectApplicationFormUseCase projectApplicationFormUseCase;
	private final FileUseCase fileUseCase;

	public DataListApiResponseDto<ProjectDetailResponseDto> getProjectList(SearchProjectListCommand projectListSearchRequestCommand, PageCommand pageCommand) {
		
		PageResult<Project> pagedProjectList = projectUseCase.getProjectList(projectListSearchRequestCommand, pageCommand);
        List<ProjectDetailResponseDto> projectDetailResponseDtoList = pagedProjectList.content().stream()
                .map(project -> ProjectDetailResponseDto.fromDomain(project, null))
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
		String imageFileUrl = null;
        Project project = projectUseCase.getProjectDetail(projectGuid);
        if(project.getImageFileGuid() != null && !project.getImageFileGuid().isBlank()) {
        	imageFileUrl = fileUseCase.find(project.getImageFileGuid()).metadata().path();
        }
        return ProjectDetailResponseDto.fromDomain(project, imageFileUrl);
	}

	public void deleteProject(String projectGuid) {
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

	public ProjectDetailWithFormResponseDto getProjectDetailWithForm(String projectGuid) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		List<String> formList = projectApplicationFormUseCase.getByProjectGuid(projectGuid);
		List<ApplicationForm> applicationFormList = applicationFormUseCase.getNoCustomizedFormById(formList);
		List<ApplicationFormCommand> additionalFormList = applicationFormUseCase.getCustomizedFormById(formList);
		
		List<String> applicationFormGuidList = applicationFormList.stream()
												.map(ApplicationForm::getApplicationFormGuid)
												.toList();
		List<ApplicationFormResponseDto> additionFormResponseDto = additionalFormList.stream()
																	.map(ApplicationFormResponseDto::fromCommand)
																	.toList();
		String imageFileName = null;
		String attachmentFileName = null;
		if(project.getImageFileGuid() != null && !project.getImageFileGuid().isBlank()) {
			imageFileName = fileUseCase.find(project.getImageFileGuid()).metadata().originalName();
        }
		if(project.getAttachmentFileGuid() != null && !project.getAttachmentFileGuid().isBlank()) {
			attachmentFileName = fileUseCase.find(project.getAttachmentFileGuid()).metadata().originalName();
        }
		// 파일 이름 조회
		return ProjectDetailWithFormResponseDto.fromDomain(project, applicationFormGuidList, additionFormResponseDto, imageFileName, attachmentFileName);
	}

}
