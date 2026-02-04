package teamdevhub.devhub.outbound.project.adapter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.core.project.port.out.ProjectRepository;
import teamdevhub.devhub.outbound.project.persistence.ProjectQueryRepository;

@Component
@RequiredArgsConstructor
public class ProjectAdapter implements ProjectRepository {
	
    private final ProjectQueryRepository projectQueryRepository;
	
	@Override
	public PageResult<ProjectDetail> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand) {
		Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size(), Sort.by("registeredDate").descending());
		
		Page<ProjectDetail> pagedProjectEntityList = projectQueryRepository.listProject(searchProjectListCommand, pageable);
		List<ProjectDetail> projectList = pagedProjectEntityList.getContent();
		
        return PageResult.of(
        		projectList,
        		pagedProjectEntityList.getNumber(),
        		pagedProjectEntityList.getSize(),
        		pagedProjectEntityList.getTotalElements());
	}
	
	@Override
	public ProjectDetail getProjectDetail(String projectGuid) {
		ProjectDetail projectEntity = projectQueryRepository.findProjectDetailByGuid(projectGuid);
		
		return projectEntity;
	}

}
