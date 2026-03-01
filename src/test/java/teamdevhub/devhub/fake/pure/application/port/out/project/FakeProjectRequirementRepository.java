package teamdevhub.devhub.fake.pure.application.port.out.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.domain.Requirement;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementCommand;
import teamdevhub.devhub.core.project.port.out.ProjectRequirementRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

public class FakeProjectRequirementRepository implements ProjectRequirementRepository {
	
	private final FakeUuidIdentifierProvider identifierProvider = new FakeUuidIdentifierProvider("PROJECT_REQUIREMENT_UUID");
	private final List<Requirement> store = new ArrayList<>();

	@Override
	public void saveAll(Set<CreateProjectRequirementCommand> positions) {
		List<Requirement> projectRequirementList = positions.stream()
				.map(item -> {
					String uuid = identifierProvider.generateIdentifier();
					return Requirement.createProjectRequirement(uuid, item);
				}).toList();
		store.addAll(projectRequirementList);
	}
	
	public List<Requirement> findByProjectGuid(String projectGuid) {
		return store.stream()
				.filter(item -> projectGuid.equals(item.getProjectGuid()))
				.toList();
	}

}
