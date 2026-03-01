package teamdevhub.devhub.outbound.project.persistence;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.outbound.project.adapter.entity.ProjectSkillEntity;

public interface JpaProjectSkillRepository extends JpaRepository<ProjectSkillEntity, String> {

	@Query("""
			select ps.projectGuid
			from ProjectSkill ps
			where (:skillCodeList is null or ps.skillCd in :skillCodeList)
			""")
	List<String> findBySkillCdList(@Param("skillCodeList")List<String> skillCodeList);

	@Query("""
			select ps
			from ProjectSkill ps
			where (:projectGuids is null or ps.projectGuid in :projectGuids)
			""")
	List<ProjectSkillEntity> findByProjectGuid(Set<String> projectGuids);

}
