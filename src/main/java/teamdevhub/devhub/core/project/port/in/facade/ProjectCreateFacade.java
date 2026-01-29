package teamdevhub.devhub.core.project.port.in.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectCreateFacade {
	
	private final ProjectUseCase projectUseCase;
	
	public void createProject(CreateProjectCommand createProjectCommand) {
		projectUseCase.createProject(createProjectCommand);
	}
}
