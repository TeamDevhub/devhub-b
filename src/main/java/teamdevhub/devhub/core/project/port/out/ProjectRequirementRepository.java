package teamdevhub.devhub.core.project.port.out;

import java.util.Set;

import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementCommand;

public interface ProjectRequirementRepository {

	void saveAll(Set<CreateProjectRequirementCommand> positions);

}
