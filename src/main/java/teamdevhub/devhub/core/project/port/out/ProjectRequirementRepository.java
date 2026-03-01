package teamdevhub.devhub.core.project.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.core.project.domain.Requirement;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectRequirementCommand;

public interface ProjectRequirementRepository {

	void saveAll(Set<CreateProjectRequirementCommand> positions);

	List<String> selectProjectGuidByPositionCodeAndPositionLevel(List<String> positionCodeList,
			List<String> positionLevelCodeList);

	List<Requirement> findByProjectGuid(Set<String> projectGuids);

}
