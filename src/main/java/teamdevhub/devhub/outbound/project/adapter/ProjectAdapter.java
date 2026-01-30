package teamdevhub.devhub.outbound.project.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.out.ProjectRepository;
import teamdevhub.devhub.outbound.project.adapter.mapper.ProjectMapper;
import teamdevhub.devhub.outbound.project.persistence.JpaProjectRepository;

@Component
@RequiredArgsConstructor
public class ProjectAdapter implements ProjectRepository {
	
	private final JpaProjectRepository jpaProjectRepository;

	@Override
	public void save(Project project) {
		jpaProjectRepository.save(ProjectMapper.toEntity(project));
	}
}
