package teamdevhub.devhub.core.project.port.in.usecase;

import teamdevhub.devhub.core.project.port.in.command.CreateProjectCommand;

public interface ProjectUseCase {

	void createProject(CreateProjectCommand createProjectCommand);
}
