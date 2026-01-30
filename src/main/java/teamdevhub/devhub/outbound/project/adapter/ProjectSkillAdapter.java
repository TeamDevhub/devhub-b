package teamdevhub.devhub.outbound.project.adapter;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.project.domain.vo.skill.ProjectSkill;
import teamdevhub.devhub.core.project.port.out.ProjectSkillRepository;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectSkillEntity;
import teamdevhub.devhub.outbound.project.adapter.mapper.ProjectSkillMapper;
import teamdevhub.devhub.outbound.project.persistence.JpaProjectSkillRepository;

@Component
@RequiredArgsConstructor
public class ProjectSkillAdapter implements ProjectSkillRepository {
	
	private final IdentifierProvider identifierProvider;
	private final JpaProjectSkillRepository jpaProjectSkillRepository;
	
	@Override
	public void saveAll(Set<ProjectSkill> skills) {
		if(skills.isEmpty()) {
			return;
		}

		List<ProjectSkillEntity> projectSkillEntityList = skills.stream()
				.map(projectSkill -> {
					String projectSkillGuid = identifierProvider.generateIdentifier();
					return ProjectSkillMapper.toEntity(projectSkillGuid, projectSkill);
				})
				.toList();
		jpaProjectSkillRepository.saveAll(projectSkillEntityList);
		
	}

}
