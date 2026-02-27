package teamdevhub.devhub.fake.pure.application.port.out.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectSkillCommand;
import teamdevhub.devhub.core.project.domain.vo.skill.ProjectSkill;
import teamdevhub.devhub.core.project.port.out.ProjectSkillRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

public class FakeProjectSkillRepository implements ProjectSkillRepository {
	
	private final FakeUuidIdentifierProvider identifierProvider = new FakeUuidIdentifierProvider("PROJECT_SKILL_UUID");
	private final List<ProjectSkill> store = new ArrayList<>();

	@Override
	public void saveAll(Set<CreateProjectSkillCommand> skills) {
		List<ProjectSkill> projectSkillList = skills.stream()
				.map(item -> {
					String uuid = identifierProvider.generateIdentifier();
					return ProjectSkill.createProjectSkill(uuid, item);
				}).toList();
		store.addAll(projectSkillList);
	}
	
	public List<ProjectSkill> findByProjectGuid(String projectGuid) {
		return store.stream()
				.filter(item -> projectGuid.equals(item.getProjectGuid()))
				.toList();
	}

}
