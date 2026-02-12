package teamdevhub.devhub.core.project.port.in.usecase;

import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectCommand;

public interface ProjectUseCase {

	void createProject(CreateProjectCommand createProjectCommand);
	
    Project getProjectDetail(String projectGuid);

}
