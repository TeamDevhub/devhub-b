package teamdevhub.devhub.core.project.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.core.project.domain.Skill;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectSkillCommand;

public interface ProjectSkillRepository {

	void saveAll(Set<CreateProjectSkillCommand> skills);

	List<String> selectProjectGuidBySkillCd(List<String> skillCodeList);

	List<Skill> findByProjectGuid(Set<String> projectGuids);

}
