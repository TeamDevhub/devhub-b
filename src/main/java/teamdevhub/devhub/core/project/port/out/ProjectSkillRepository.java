package teamdevhub.devhub.core.project.port.out;

import java.util.Set;

import teamdevhub.devhub.core.project.domain.vo.skill.ProjectSkill;

public interface ProjectSkillRepository {

	void saveAll(Set<ProjectSkill> skills);

}
