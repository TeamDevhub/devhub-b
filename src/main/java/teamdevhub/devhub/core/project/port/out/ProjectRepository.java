package teamdevhub.devhub.core.project.port.out;


import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;

public interface ProjectRepository {

	void save(Project project);
    Project getProjectDetail(String projectGuid);
	PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand);
	void deleteById(String projectGuid);
	void update(Project upateProject);
	PageResult<Project> getUserProjects(String userGuid, PageCommand pageCommand);
	void closeProject(String projectGuid);
}
