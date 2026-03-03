package teamdevhub.devhub.outbound.project.persistence;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;

public interface JpaProjectRequirementRepository extends JpaRepository<ProjectRequirementEntity, String> {

	@Query("""
			select pr.projectGuid
			from ProjectRequirementEntity pr
			where (:positionCodeList is null or pr.positionCd in :positionCodeList)
			and	  (:positionLevelCodeList is null or pr.levelCd in :positionLevelCodeList)
			""")
	List<String> findByPositionCdAndLevelCd(List<String> positionCodeList, List<String> positionLevelCodeList);

	@Query("""
			select pr
			from ProjectRequirementEntity pr
			where (:projectGuids is null or pr.projectGuid in :projectGuids)
			""")
	List<ProjectRequirementEntity> findByProjectGuid(Set<String> projectGuids);

}
