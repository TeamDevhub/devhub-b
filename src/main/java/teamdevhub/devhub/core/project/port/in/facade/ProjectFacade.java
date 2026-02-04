package teamdevhub.devhub.core.project.port.in.facade;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.in.usecase.ProjectUseCase;

@Service
@RequiredArgsConstructor
public class ProjectFacade {
	
	private final ProjectUseCase projectUseCase;

	public PageResult<ProjectDetail> getProjectList(SearchProjectListCommand projectListSearchRequestDto, PageCommand pageCommand) {
		PageResult<ProjectDetail> result = projectUseCase.getProjectList(projectListSearchRequestDto, pageCommand);
		
		return result;
	}

	public ProjectDetail getProjectDetail(String projectGuid) {
		ProjectDetail result = projectUseCase.getProjectDetail(projectGuid);
		
		return result;
	}

}
