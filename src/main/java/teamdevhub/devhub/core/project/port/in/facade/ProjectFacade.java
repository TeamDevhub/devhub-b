package teamdevhub.devhub.core.project.port.in.facade;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.in.command.ApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.facade.model.ApplicationFormResponseDto;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.file.port.in.usecase.FileUseCase;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.ProjectLike;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectLikeCommand;
import teamdevhub.devhub.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailWithFormResponseDto;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectApplicationFormUseCase;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectLikeUseCase;
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
	private final ProjectLikeUseCase projectLikeUseCase;

	public DataListApiResponseDto<ProjectDetailResponseDto> getProjectList(SearchProjectListCommand projectListSearchRequestCommand, PageCommand pageCommand, String userGuid) {
		
		PageResult<Project> pagedProjectList = projectUseCase.getProjectList(projectListSearchRequestCommand, pageCommand);
        List<ProjectDetailResponseDto> projectDetailResponseDtoList = pagedProjectList.content().stream()
                .map(project -> {
                	ProjectLike projectLike = projectLikeUseCase.findByProjectGuidAndUserGuid(project.getProjectGuid(), userGuid);
                	boolean isProjectLiked = false;
                	if(projectLike != null) isProjectLiked= true;
                	return ProjectDetailResponseDto.fromDomain(project, null, isProjectLiked);
                })
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

	public ProjectDetailResponseDto getProjectDetail(String projectGuid, String userGuid) {
		String imageFileUrl = null;
        Project project = projectUseCase.getProjectDetail(projectGuid);
        if(project.getImageFileGuid() != null && !project.getImageFileGuid().isBlank()) {
        	imageFileUrl = fileUseCase.find(project.getImageFileGuid()).metadata().path();
        }
        ProjectLike projectLike = projectLikeUseCase.findByProjectGuidAndUserGuid(project.getProjectGuid(), userGuid);
    	boolean isProjectLiked = false;
    	if(projectLike != null) isProjectLiked= true;
        return ProjectDetailResponseDto.fromDomain(project, imageFileUrl, isProjectLiked);
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
		String email = userProfileUseCase.getUserInfo(project.getUserGuid()).getEmail();

		List<ProjectApplicationForm> projectForms = projectApplicationFormUseCase.findByProjectGuid(projectGuid);
		List<String> applicationFormGuids = projectForms.stream()
				.map(ProjectApplicationForm::getApplicationFormGuid)
				.toList();

		Map<String, String> applicationFormGuidToProjectFormGuid = projectForms.stream()
				.collect(Collectors.toMap(
						ProjectApplicationForm::getApplicationFormGuid,
						ProjectApplicationForm::getProjectApplicationFormGuid
				));

		List<ApplicationForm> standardForms = applicationFormUseCase.getNoCustomizedFormById(applicationFormGuids);
		List<ApplicationFormCommand> customForms = applicationFormUseCase.getCustomizedFormById(applicationFormGuids);

		List<ApplicationFormResponseDto> applicationFormResponseList = standardForms.stream()
				.map(form -> ApplicationFormResponseDto.builder()
						.projectApplicationFormGuid(applicationFormGuidToProjectFormGuid.get(form.getApplicationFormGuid()))
						.applicationFormGuid(form.getApplicationFormGuid())
						.typeCd(form.getTypeCd())
						.title(form.getTitle())
						.helpText(form.getHelpText())
						.isCustomized(false)
						.isUsed(form.isUsed())
						.build())
				.toList();

		List<ApplicationFormResponseDto> additionalFormResponseList = customForms.stream()
				.map(cmd -> ApplicationFormResponseDto.builder()
						.projectApplicationFormGuid(applicationFormGuidToProjectFormGuid.get(cmd.getApplicationFormGuid()))
						.applicationFormGuid(cmd.getApplicationFormGuid())
						.typeCd(cmd.getTypeCd())
						.title(cmd.getTitle())
						.helpText(cmd.getHelpText())
						.isCustomized(true)
						.isUsed(cmd.isUsed())
						.itemList(cmd.getItemList())
						.build())
				.toList();
		
		String imageFileName = null;
		String attachmentFileName = null;
		if(project.getImageFileGuid() != null && !project.getImageFileGuid().isBlank()) {
			imageFileName = fileUseCase.find(project.getImageFileGuid()).metadata().originalName();
        }
		if(project.getAttachmentFileGuid() != null && !project.getAttachmentFileGuid().isBlank()) {
			attachmentFileName = fileUseCase.find(project.getAttachmentFileGuid()).metadata().originalName();
        }
		return ProjectDetailWithFormResponseDto.fromDomain(project, email, applicationFormResponseList, additionalFormResponseList, imageFileName, attachmentFileName);
	}

	public void toggleProjectLike(CreateProjectLikeCommand createProjectLikeCommand) {
		projectLikeUseCase.toggleProjectLike(createProjectLikeCommand);
	}

}
