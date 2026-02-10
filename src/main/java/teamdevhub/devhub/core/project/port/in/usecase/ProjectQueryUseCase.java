package teamdevhub.devhub.core.project.port.in.usecase;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;

public interface ProjectQueryUseCase {

    PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand);
}

