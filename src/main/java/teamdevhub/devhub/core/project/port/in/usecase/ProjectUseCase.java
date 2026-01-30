package teamdevhub.devhub.core.project.port.in.usecase;

import org.springframework.web.multipart.MultipartFile;

import teamdevhub.devhub.core.project.port.in.command.CreateProjectCommand;

public interface ProjectUseCase {

	void createProject(CreateProjectCommand createProjectCommand, MultipartFile attachment, MultipartFile image);
}
