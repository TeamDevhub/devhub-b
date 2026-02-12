package teamdevhub.devhub.outbound.project.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.out.ProjectRepository;
import teamdevhub.devhub.outbound.project.adapter.mapper.ProjectMapper;
import teamdevhub.devhub.outbound.project.persistence.JpaProjectRepository;
import teamdevhub.devhub.outbound.project.persistence.ProjectQueryDao;

@Component
@RequiredArgsConstructor
public class ProjectAdapter implements ProjectRepository {
	
	private final JpaProjectRepository jpaProjectRepository;
    private final ProjectQueryDao projectQueryDao;

	@Override
	public void save(Project project) {
		jpaProjectRepository.save(ProjectMapper.toEntity(project));
	}
	
	@Override
	public Project getProjectDetail(String projectGuid) {
        Project projectEntity = projectQueryDao.findProjectDetailByGuid(projectGuid);
		
		return projectEntity;
	}

}
