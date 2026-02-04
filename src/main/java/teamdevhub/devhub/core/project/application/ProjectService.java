package teamdevhub.devhub.core.project.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.core.project.port.out.ProjectRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService implements ProjectUseCase {
	
	private final ProjectRepository projectRepository;

	@Override
	public PageResult<ProjectDetail> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand) {
		return projectRepository.getProjectList(searchProjectListCommand, pageCommand);
	}
	
	@Override
	public ProjectDetail getProjectDetail(String projectGuid) {
		return projectRepository.getProjectDetail(projectGuid);
	}

}
