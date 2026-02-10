package teamdevhub.devhub.core.project.port.out;

import java.util.Set;

import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectSkillCommand;

public interface ProjectSkillRepository {

	void saveAll(Set<CreateProjectSkillCommand> skills);

}
