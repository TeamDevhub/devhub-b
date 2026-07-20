package teamdevhub.devhub.core.project.port.in.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.in.command.ApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.facade.model.ApplicationFormResponseDto;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.file.port.in.usecase.FileUseCase;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.ProjectLike;
import teamdevhub.devhub.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectLikeCommand;
import teamdevhub.devhub.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailWithFormResponseDto;
import teamdevhub.devhub.core.project.port.in.facade.model.UserProjectResponseDto;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectApplicationFormUseCase;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectLikeUseCase;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.port.in.usecase.UserProfileUseCase;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Service
@RequiredArgsConstructor
public class ProjectFacade {
	
	private final ProjectUseCase projectUseCase;
	private final ApplicationFormUseCase applicationFormUseCase;
	private final UserProfileUseCase userProfileUseCase;
	private final ProjectApplicationFormUseCase projectApplicationFormUseCase;
	private final FileUseCase fileUseCase;
	private final ProjectLikeUseCase projectLikeUseCase;
	private final ProjectApplicationUseCase projectApplicationUseCase;
	private final ProjectApplicationQueryUseCase projectApplicationQueryUseCase;
	private final EmailUserCredentialRepository emailUserCredentialRepository;

	public PageResult<ProjectDetailResponseDto> getProjectList(SearchProjectListCommand projectListSearchRequestCommand, PageCommand pageCommand, AuthenticatedUser user) {
		PageResult<Project> pagedProjectList = projectUseCase.getProjectList(projectListSearchRequestCommand, pageCommand);
		List<ProjectDetailResponseDto> projectDetailResponseDtoList = new ArrayList<>();
		if(user == null) {
			projectDetailResponseDtoList = pagedProjectList.content().stream()
            .map(project -> ProjectDetailResponseDto.fromDomain(project, null, false))
            .toList();
		} else {
	        projectDetailResponseDtoList = pagedProjectList.content().stream()
            .map(project -> {
            	ProjectLike projectLike = projectLikeUseCase.findByProjectGuidAndUserGuid(project.getProjectGuid(), user.userGuid());
            	boolean isProjectLiked = projectLike != null;
                return ProjectDetailResponseDto.fromDomain(project, null, isProjectLiked);
            })
            .toList();
		}

        return PageResult.of(projectDetailResponseDtoList, pagedProjectList.page(), pagedProjectList.size(), pagedProjectList.totalElements());
	}
	
	public void createProject(CreateProjectCommand createProjectCommand) {
		User user = userProfileUseCase.getUserInfo(createProjectCommand.userGuid());
		List<String> additionalFormGuidList = applicationFormUseCase.saveApplicationForms(createProjectCommand.additionalFormList());
		createProjectCommand.applicationFormList().addAll(additionalFormGuidList);
		projectUseCase.createProject(createProjectCommand, user.getUsername());
	}

	public ProjectDetailResponseDto getProjectDetail(String projectGuid, AuthenticatedUser user) {
		String imageFileUrl = null;
        Project project = projectUseCase.getProjectDetail(projectGuid);
        if(project.getImageFileGuid() != null && !project.getImageFileGuid().isBlank()) {
        	imageFileUrl = fileUseCase.find(project.getImageFileGuid()).metadata().path();
        }
        String userFileGuid = userProfileUseCase.getUserInfo(project.getUserGuid()).getFileGuid();
        var approvedCountResolver = ProjectDetailResponseDto.approvedCountResolverOf(resolveApprovedCountByRequirement(project));
        if(user == null) {
            	return ProjectDetailResponseDto.fromDomain(project, imageFileUrl, false, userFileGuid, approvedCountResolver);
		} else {
			ProjectLike projectLike = projectLikeUseCase.findByProjectGuidAndUserGuid(project.getProjectGuid(), user.userGuid());
	    	boolean isProjectLiked = projectLike != null;
            return ProjectDetailResponseDto.fromDomain(project, imageFileUrl, isProjectLiked, userFileGuid, approvedCountResolver);
		}
	}

	// 프로젝트의 모집 포지션(requirement)별 승인된 지원자 수 - 현재 모집인원 계산용
	private Map<String, Long> resolveApprovedCountByRequirement(Project project) {
		if (project.getProjectRequirement() == null || project.getProjectRequirement().isEmpty()) {
			return Map.of();
		}
		List<String> requirementGuids = project.getProjectRequirement().stream()
			.map(ProjectRequirement::getProjectRequirementGuid)
			.toList();
		return projectApplicationQueryUseCase.countApprovedByRequirementGuids(requirementGuids);
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

	public void updateProject(String projectGuid, UpdateProjectCommand updateProjectCommand, AuthenticatedUser authenticatedUser) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		if(!(UserRole.ADMIN.equals(authenticatedUser.userRole()) || project.getUserGuid().equals(authenticatedUser.userGuid()))) {
			throw BusinessRuleException.of(ErrorCode.UPDATE_FAIL);
		}
		List<String> additionalFormGuidList = applicationFormUseCase.saveApplicationForms(updateProjectCommand.additionalFormList());
		updateProjectCommand.applicationFormList().addAll(additionalFormGuidList);
		List<String> deleteApplicationFormGuids = projectUseCase.updateProject(projectGuid, updateProjectCommand);
		applicationFormUseCase.deleteApplicationForms(deleteApplicationFormGuids);
	}

	public ProjectDetailWithFormResponseDto getProjectDetailWithForm(String projectGuid) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		// 소셜 로그인 전용 회원은 이메일 자격증명이 없을 수 있으므로 없으면 null로 응답한다 (BoardService.detailBoard와 동일한 패턴)
		String email = emailUserCredentialRepository.findByUserGuid(project.getUserGuid())
				.map(EmailUserCredential::getEmail)
				.orElse(null);

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
		Map<String, Long> approvedCountByRequirement = resolveApprovedCountByRequirement(project);
		return ProjectDetailWithFormResponseDto.fromDomain(project, email, applicationFormResponseList, additionalFormResponseList, imageFileName, attachmentFileName, approvedCountByRequirement);
	}

	public void toggleProjectLike(CreateProjectLikeCommand createProjectLikeCommand) {
		projectLikeUseCase.toggleProjectLike(createProjectLikeCommand);
	}

	public PageResult<UserProjectResponseDto> getUserProjects(String userGuid, PageCommand pageCommand) {
		PageResult<Project> pagedProjectList = projectUseCase.getUserProjects(userGuid, pageCommand);
		List<UserProjectResponseDto> userProjectResponseDtoList = pagedProjectList.content().stream()
	            .map(item -> {
	            	PageResult<ProjectApplication> pagedApplicatgionList = projectApplicationQueryUseCase.getApplicationsByProjectGuid(item.getProjectGuid(), new PageCommand(0, Integer.MAX_VALUE));
	            	Project project = projectUseCase.getProjectDetail(item.getProjectGuid());
	            	return UserProjectResponseDto.fromDomain(project, pagedApplicatgionList.content().stream().map(application -> ProjectApplicationScore.toApplicationWithScore(application, 0.0)).toList(), null);
	            })
	            .toList();
		return PageResult.of(userProjectResponseDtoList, pagedProjectList.page(), pagedProjectList.size(), pagedProjectList.totalElements());
	}

	public PageResult<UserProjectResponseDto> getUserLikeProjects(String userGuid, PageCommand pageCommand) {
		PageResult<ProjectLike> pagedLikeProjectList = projectLikeUseCase.findByUserGuid(userGuid, pageCommand);
		List<UserProjectResponseDto> userProjectResponseDtoList = pagedLikeProjectList.content().stream()
            .map(projectLike -> {
            	Project project = projectUseCase.getProjectDetail(projectLike.getProjectGuid());
            	return UserProjectResponseDto.fromDomain(project, null, null);
            })
            .toList();
		return PageResult.of(userProjectResponseDtoList, pagedLikeProjectList.page(), pagedLikeProjectList.size(), pagedLikeProjectList.totalElements());
	}

	public PageResult<UserProjectResponseDto> getUserApplyProjects(String userGuid, PageCommand pageCommand) {
		PageResult<ProjectApplication> pagedApplyProjectList = projectApplicationUseCase.findByApplicantGuid(userGuid, pageCommand);
		List<UserProjectResponseDto> userProjectResponseDtoList = pagedApplyProjectList.content().stream()
            .map(projectApply -> {
            	Project project = projectUseCase.getProjectByRequirementGuid(projectApply.getRequirementGuid());
            	Project projectDetail = projectUseCase.getProjectDetail(project.getProjectGuid());
            	return UserProjectResponseDto.fromDomain(projectDetail, null, projectApply.getStatusCd(), projectApply.getApplicationGuid());
            })
            .toList();
		return PageResult.of(userProjectResponseDtoList, pagedApplyProjectList.page(), pagedApplyProjectList.size(), pagedApplyProjectList.totalElements());
	}

	public void closeProject(String projectGuid, AuthenticatedUser authenticatedUser) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		if(!(UserRole.ADMIN.equals(authenticatedUser.userRole()) || project.getUserGuid().equals(authenticatedUser.userGuid()))) {
			throw BusinessRuleException.of(ErrorCode.UPDATE_FAIL);
		}
		projectUseCase.closeProject(projectGuid);
	}

	public PageResult<UserProjectResponseDto> getUserParticipateProjects(String userGuid, PageCommand pageCommand) {
		PageResult<Project> pagedParticipateProjectList = projectUseCase.getEndProjectsByApplicantGuid(userGuid, pageCommand);
		List<UserProjectResponseDto> userProjectResponseDtoList = pagedParticipateProjectList.content().stream()
            .map(participateProject -> {
            	List<ProjectApplicationScore> applicationList = projectApplicationUseCase.findAcceptedByProjectGuid(participateProject.getProjectGuid());
            	Project project = projectUseCase.getProjectDetail(participateProject.getProjectGuid());
            	return UserProjectResponseDto.fromDomain(project, applicationList, null);
            })
            .toList();
		return PageResult.of(userProjectResponseDtoList, pagedParticipateProjectList.page(), pagedParticipateProjectList.size(), pagedParticipateProjectList.totalElements());
	}

}
