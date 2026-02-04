package teamdevhub.devhub.core.project.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.project.port.out.ProjectRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService implements ProjectUseCase {
	
	private final ProjectRepository projectRepository;

	@Override
	public PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand) {
		return projectRepository.getProjectList(searchProjectListCommand, pageCommand);
	}
}
