package teamdevhub.devhub.fake.pure.application.port.out.project;

import java.util.ArrayList;
import java.util.List;

import teamdevhub.devhub.core.project.domain.Project;
import teamdevhub.devhub.core.project.port.out.ProjectRepository;

public class FakeProjectRepository implements ProjectRepository {
	
	private final List<Project> store = new ArrayList<>();

	@Override
	public void save(Project project) {
		store.add(project);
	}

	@Override
	public Project getProjectDetail(String projectGuid) {
		return store.stream()
				.filter(item -> projectGuid.equals(item.getProjectGuid()))
				.findAny()
				.get();
	}

}
