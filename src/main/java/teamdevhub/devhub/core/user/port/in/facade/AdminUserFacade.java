package teamdevhub.devhub.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.core.auth.port.in.usecase.UserCredentialUseCase;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.facade.model.UserProjectResponseDto;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.report.domain.Report;
import teamdevhub.devhub.core.report.port.in.usecase.ReportQueryUseCase;
import teamdevhub.devhub.core.report.port.in.usecase.ReportUseCase;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.port.in.command.AdminUpdateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.BanUserCommand;
import teamdevhub.devhub.core.user.port.in.usecase.AdminUserManagementUseCase;
import teamdevhub.devhub.core.user.port.in.usecase.UserProfileUseCase;
import teamdevhub.devhub.core.user.port.in.usecase.UserQueryUseCase;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.core.user.port.in.facade.model.UserBasicResponseDto;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminUserFacade {

    private final UserQueryUseCase userQueryUseCase;
    private final UserProfileUseCase userProfileUseCase;
    private final AdminUserManagementUseCase adminUserManagementUseCase;
    private final UserCredentialUseCase userCredentialUseCase;
    private final ProjectUseCase projectUseCase;
    private final ProjectApplicationUseCase projectApplicationUseCase;
    private final ReportQueryUseCase reportQueryUseCase;
    private final ReportUseCase reportUseCase;
    private final ProjectApplicationQueryUseCase projectApplicationQueryUseCase;

    public PageResult<UserBasicResponseDto> listUsers(SearchUserCommand searchUserCommand, PageCommand pageCommand) {
        PageResult<User> result = userQueryUseCase.listUser(searchUserCommand, pageCommand);
        List<UserBasicResponseDto> userBasicResponseDtoList = result.content().stream()
                .map(UserBasicResponseDto::fromDomain)
                .toList();
        return PageResult.of(userBasicResponseDtoList, result.page(), result.size(), result.totalElements());
    }

    public User getUserDetail(String userGuid) {
        return userProfileUseCase.getCurrentUserProfile(userGuid);
    }

    public void updateUser(AdminUpdateUserCommand adminUpdateUserCommand) {
        adminUserManagementUseCase.updateUser(adminUpdateUserCommand);
    }

    public void resetUserPassword(String userGuid, String newPassword) {
        userCredentialUseCase.resetUserPassword(userGuid, newPassword);
    }

    public void banUser(BanUserCommand banUserCommand) {
        adminUserManagementUseCase.banUser(banUserCommand);
    }

    public void unbanUser(String userGuid) {
        adminUserManagementUseCase.unbanUser(userGuid);
    }

    /**
     * approval state 의미 및 필요성
     * @param userGuid
     * @param pageCommand
     * @return
     */
    public PageResult<UserProjectResponseDto> getUserProjects(String userGuid, PageCommand pageCommand) {
    	List<UserProjectResponseDto> userProjectResponseDtoList = new ArrayList<>();
    	PageResult<Project> result = projectUseCase.getUserProjects(userGuid, pageCommand);
        userProjectResponseDtoList = result.content().stream()
	            .map(item -> {
	            	PageResult<ProjectApplication> pagedApplicatgionList = projectApplicationQueryUseCase.getApplicationsByProjectGuid(item.getProjectGuid(), new PageCommand(0, Integer.MAX_VALUE));
	            	Project project = projectUseCase.getProjectDetail(item.getProjectGuid());
	            	return UserProjectResponseDto.fromDomain(project, pagedApplicatgionList.content().stream().map(application -> ProjectApplicationScore.toApplicationWithScore(application, 0.0)).toList(), null);
	            })
	            .toList();
		return PageResult.of(userProjectResponseDtoList, result.page(), result.size(), result.totalElements());
    }

    public PageResult<UserProjectResponseDto> getUserApplyProjects(String userGuid, PageCommand pageCommand) {
    	List<UserProjectResponseDto> userProjectResponseDtoList = new ArrayList<>();
		PageResult<ProjectApplication> pagedApplyProjectList = projectApplicationUseCase.findByApplicantGuid(userGuid, pageCommand);
		userProjectResponseDtoList = pagedApplyProjectList.content().stream()
	            .map(projectApply -> {
	            	Project project = projectUseCase.getProjectByRequirementGuid(projectApply.getRequirementGuid());
	            	Project projectDetail = projectUseCase.getProjectDetail(project.getProjectGuid());
	            	return UserProjectResponseDto.fromDomain(projectDetail, null, projectApply.getStatusCd());
	            })
	            .toList();
		return PageResult.of(userProjectResponseDtoList, pagedApplyProjectList.page(), pagedApplyProjectList.size(), pagedApplyProjectList.totalElements());
    }

    public PageResult<Report> getUserReceivedReports(String userGuid, PageCommand pageCommand) {
        return reportQueryUseCase.getReportsByReportedUser(userGuid, pageCommand);
    }

    public PageResult<Report> getUserSubmittedReports(String userGuid, PageCommand pageCommand) {
        return reportQueryUseCase.getReportsByReporterUser(userGuid, pageCommand);
    }

    public PageResult<Report> getAllReports(PageCommand pageCommand) {
        return reportQueryUseCase.getAllReports(pageCommand);
    }

    public void processReport(String reportGuid) {
        reportUseCase.processReport(reportGuid);
    }
}
