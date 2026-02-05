package teamdevhub.devhub.core.project.port.out;

import java.util.Set;

import teamdevhub.devhub.core.project.domain.vo.requirement.ProjectRequirement;

public interface ProjectRequirementRepository {

	void saveAll(Set<ProjectRequirement> positions);

}
