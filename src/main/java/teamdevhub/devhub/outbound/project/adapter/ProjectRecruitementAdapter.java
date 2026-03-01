package teamdevhub.devhub.outbound.project.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.project.domain.Requirement;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementCommand;
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
	public void saveAll(Set<CreateProjectRequirementCommand> positions) {
		if(positions.isEmpty()) {
			return;
		}

		List<ProjectRequirementEntity> projectRequirementEntityList = positions.stream()
				.map(projectRequirementCommand -> {
					String projectRequirementGuid = identifierProvider.generateIdentifier();
					Requirement projectRequirement = Requirement.createProjectRequirement(projectRequirementGuid, projectRequirementCommand);
					return ProjectRequirementMapper.toEntity(projectRequirement);
				})
				.toList();
		jpaProjectRequirementRepository.saveAll(projectRequirementEntityList);

	}
	@Override
	public List<String> selectProjectGuidByPositionCodeAndPositionLevel(List<String> positionCodeList,
			List<String> positionLevelCodeList) {
		List<String> projectGuids = new ArrayList<>();
		if(positionCodeList == null && positionLevelCodeList == null) {
			return projectGuids;
		}
		return jpaProjectRequirementRepository.findByPositionCdAndLevelCd(positionCodeList, positionLevelCodeList);
	}
	@Override
	public List<Requirement> findByProjectGuid(Set<String> projectGuids) {
		List<ProjectRequirementEntity> entityList = jpaProjectRequirementRepository.findByProjectGuid(projectGuids);
		return entityList.stream()
				.map(ProjectRequirementMapper::toProjectRequirement)
				.toList();
	}

}
