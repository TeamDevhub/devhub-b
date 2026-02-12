package teamdevhub.devhub.core.project.port.out;

import teamdevhub.devhub.core.project.domain.Project;

public interface ProjectRepository {

	void save(Project project);
    Project getProjectDetail(String projectGuid);

}
