package teamdevhub.devhub.outbound.project.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.core.project.domain.ProjectSkill;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectSkillCommand;
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
	public void saveAll(Set<CreateProjectSkillCommand> skills) {
		if(skills.isEmpty()) {
			return;
		}

		List<ProjectSkillEntity> projectSkillEntityList = skills.stream()
				.map(projectSkillCommand -> {
					String projectSkillGuid = identifierProvider.generateIdentifier();
					ProjectSkill projectSkill =  ProjectSkill.createProjectSkill(projectSkillGuid, projectSkillCommand);
					return ProjectSkillMapper.toEntity(projectSkill);
				})
				.toList();
		jpaProjectSkillRepository.saveAll(projectSkillEntityList);
		
	}

	@Override
	public List<String> selectProjectGuidBySkillCd(List<String> skillCodeList) {
		List<String> projectGuids = new ArrayList<>();
		if(skillCodeList == null) {
			return projectGuids;
		}
		return jpaProjectSkillRepository.findBySkillCdList(skillCodeList);
	}

	@Override
	public List<ProjectSkill> findByProjectGuid(Set<String> projectGuids) {
		List<ProjectSkillEntity> entityList = jpaProjectSkillRepository.findByProjectGuid(projectGuids);
		return entityList.stream()
				.map(ProjectSkillMapper::toProjectSkill)
				.toList();
	}

}
