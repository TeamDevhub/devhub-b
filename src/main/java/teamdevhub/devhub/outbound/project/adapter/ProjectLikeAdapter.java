package teamdevhub.devhub.outbound.project.adapter;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.project.domain.ProjectLike;
import teamdevhub.devhub.core.project.port.out.ProjectLikeRepository;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectLikeEntity;
import teamdevhub.devhub.outbound.project.adapter.mapper.ProjectMapper;
import teamdevhub.devhub.outbound.project.persistence.JpaProjectLikeRepository;

@Component
@RequiredArgsConstructor
public class ProjectLikeAdapter implements ProjectLikeRepository {
	
	private static JpaProjectLikeRepository jpaProjectLikeRepository;

	@Override
	public List<ProjectLike> findByProjectGuid(Set<String> projectGuids) {
		List<ProjectLikeEntity> entityList = jpaProjectLikeRepository.findByProjectGuids(projectGuids);
		return entityList.stream()
				.map(ProjectMapper::toProjectLike)
				.toList();
	}

}
