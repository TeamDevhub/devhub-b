package teamdevhub.devhub.core.project.port.out;

import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;

public interface ProjectRepository {

	void save(Project project);
	PageResult<ProjectDetail> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand);
}
