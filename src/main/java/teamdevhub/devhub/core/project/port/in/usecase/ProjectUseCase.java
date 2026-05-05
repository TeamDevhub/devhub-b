package teamdevhub.devhub.core.project.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;

public interface ProjectUseCase {

	void createProject(CreateProjectCommand createProjectCommand, String username);
	
	PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand);
	
    Project getProjectDetail(String projectGuid);

	List<String> deleteProject(String projectGuid);

	List<String> updateProject(String projectGuid, UpdateProjectCommand updateProjectCommand);

	PageResult<Project> getUserProjects(String userGuid, PageCommand pageCommand);

	void closeProject(String projectGuid);

	PageResult<Project> getEndProjectsByApplicantGuid(String userGuid, PageCommand pageCommand);

	Project getProjectByRequirementGuid(String requirementGuid);

}
