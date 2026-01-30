package teamdevhub.devhub.outbound.project.adapter;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.project.domain.vo.requirement.ProjectRequirement;
import teamdevhub.devhub.core.project.port.out.ProjectRequirementRepository;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.outbound.project.adapter.mapper.ProjectRequirementMapper;
import teamdevhub.devhub.outbound.project.persistence.JpaProjectRequirementRepository;

@Component
@RequiredArgsConstructor
public class ProjectRecruitementAdapter implements ProjectRequirementRepository {
	
	private final IdentifierProvider identifierProvider;
	private final JpaProjectRequirementRepository jpaProjectRequirementRepository;
	@Override
	public void saveAll(Set<ProjectRequirement> positions) {
		if(positions.isEmpty()) {
			return;
		}

		List<ProjectRequirementEntity> projectRequirementEntityList = positions.stream()
				.map(projectRequirement -> {
					String projectRequirementGuid = identifierProvider.generateIdentifier();
					return ProjectRequirementMapper.toEntity(projectRequirementGuid, projectRequirement);
				})
				.toList();
		jpaProjectRequirementRepository.saveAll(projectRequirementEntityList);

	}

}
