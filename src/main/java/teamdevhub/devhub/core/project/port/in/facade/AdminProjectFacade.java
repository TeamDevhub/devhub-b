package teamdevhub.devhub.core.project.port.in.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.file.port.in.usecase.FileUseCase;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.ProjectLike;
import teamdevhub.devhub.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectApplicationFormUseCase;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectLikeUseCase;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.port.in.usecase.UserProfileUseCase;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Service
@RequiredArgsConstructor
public class AdminProjectFacade {
	
	private final ProjectUseCase projectUseCase;
	private final ApplicationFormUseCase applicationFormUseCase;
	private final UserProfileUseCase userProfileUseCase;
	private final ProjectApplicationFormUseCase projectApplicationFormUseCase;
	private final FileUseCase fileUseCase;
	private final ProjectLikeUseCase projectLikeUseCase;
	private final ProjectApplicationUseCase projectApplicationUseCase;
	private final ProjectApplicationQueryUseCase projectApplicationQueryUseCase;

	public PageResult<ProjectDetailResponseDto> getProjectList(SearchProjectListCommand projectListSearchRequestCommand,
			PageCommand pageCommand, AuthenticatedUser user) {
		PageResult<Project> pagedProjectList = projectUseCase.getProjectList(projectListSearchRequestCommand, pageCommand);
		List<ProjectDetailResponseDto> projectDetailResponseDtoList = new ArrayList<>();
		if(user == null) {
			projectDetailResponseDtoList = pagedProjectList.content().stream()
            .map(project -> {
            	return ProjectDetailResponseDto.fromDomain(project, null, false);
            })
            .toList();
		} else {
	        projectDetailResponseDtoList = pagedProjectList.content().stream()
            .map(project -> {
            	ProjectLike projectLike = projectLikeUseCase.findByProjectGuidAndUserGuid(project.getProjectGuid(), user.userGuid());
            	boolean isProjectLiked = false;
            	if(projectLike != null) isProjectLiked= true;
            	return ProjectDetailResponseDto.fromDomain(project, null, isProjectLiked);
            })
            .toList();
		}
		PageResult<ProjectDetailResponseDto> pagedProjectDetail = PageResult.of(projectDetailResponseDtoList, pagedProjectList.page(), pagedProjectList.size(), pagedProjectList.totalElements());
		
		return pagedProjectDetail;
	}

	public ProjectDetailResponseDto getProjectDetail(String projectGuid, AuthenticatedUser user) {
		String imageFileUrl = null;
        Project project = projectUseCase.getProjectDetail(projectGuid);
        if(project.getImageFileGuid() != null && !project.getImageFileGuid().isBlank()) {
        	imageFileUrl = fileUseCase.find(project.getImageFileGuid()).metadata().path();
        }
    	return ProjectDetailResponseDto.fromDomain(project, imageFileUrl, false);
	}

	public void updateProject(String projectGuid, UpdateProjectCommand updateProjectCommand) {
		List<String> additionalFormGuidList = applicationFormUseCase.saveApplicationForms(updateProjectCommand.additionalFormList());
		updateProjectCommand.applicationFormList().addAll(additionalFormGuidList);
		List<String> deleteApplicationFormGuids = projectUseCase.updateProject(projectGuid, updateProjectCommand);
		applicationFormUseCase.deleteApplicationForms(deleteApplicationFormGuids);
		
	}

	public void deleteProject(String projectGuid, AuthenticatedUser authenticatedUser) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		if(!(UserRole.ADMIN.equals(authenticatedUser.userRole()) || project.getUserGuid().equals(authenticatedUser.userGuid()))) {
			throw BusinessRuleException.of(ErrorCode.DELETE_FAIL);
		}
		// 프로젝트 지원자 조회 후 지원자가 있으면 return, 지원자 없으면 continue??
		// 삭제해야할 신청폼 목록 반환?
		List<String> deleteApplicationFormGuids =  projectUseCase.deleteProject(projectGuid);
		applicationFormUseCase.deleteApplicationForms(deleteApplicationFormGuids);
	}

}
