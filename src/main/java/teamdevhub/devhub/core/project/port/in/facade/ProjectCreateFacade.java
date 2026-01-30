package teamdevhub.devhub.core.project.port.in.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.project.port.in.command.CreateProjectCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectCreateFacade {
	
	private final ProjectUseCase projectUseCase;
	
	public void createProject(CreateProjectCommand createProjectCommand, MultipartFile attachment, MultipartFile image) {
		projectUseCase.createProject(createProjectCommand, attachment, image);
	}
}
