package teamdevhub.devhub.outbound.project.persistence;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.outbound.project.adapter.entity.ProjectEntity;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, String> {

	@Query("""
			select p
			from Project p
			where exists (
				select 1
				from ProjectRequirement pr
				where pr.projectGuid = p.projectGuid
				and (:positionCodeList is null or pr.positionCd in :positionCodeList)
				and (:positionLevelCodeList is null or pr.levelCd in :positionLevelCodeList)
				)
			and (:skillCodeList is null or exists (
				select 1
				from ProjectSkilㅣ ps
				where ps.projectGuid = p.projectGuid
				and ps.skillCd in :skillCodeList
				))
			and (:keyword is null or (
		        p.title like concat('%', :keyword, '%')
		        or p.content like concat('%', :keyword, '%')
		    ))
			and (:regionCodeList is null or p.progressRegionCd in :regionCodeList)
			and (:projectRecruitTypeList is null or p.recruitmentTypeCd in :projectRecruitTypeList)
			and (:projectProgressTypeList is null or p.progressTypeCd in :projectProgressTypeList)
			and (:recruitmentStartDate is null or p.recruitmentStartDate >= :recruitmentStartDate)
			and (:recruitmentEndDate is null or p.recruitmentEndDate <= :recruitmentEndDate)
			and (:progressStartDate is null or p.progressStartDate >= :progressStartDate)
			""")
	Page<ProjectEntity> findBySearchCondition(@Param("order") String order, @Param("keyword") String keyword, @Param("skillCodeList") List<String> skillCodeList,
			@Param("regionCodeList") List<String> regionCodeList, @Param("positionCodeList") List<String> positionCodeList, @Param("positionLevelCodeList") List<String> positionLevelCodeList,
			@Param("projectRecruitTypeList") List<String> projectRecruitTypeList, @Param("projectRecruitStatusList") List<String> projectRecruitStatusList,
			@Param("projectProgressTypeList") List<String> projectProgressTypeList, @Param("recruitmentStartDate") LocalDate recruitmentStartDate, @Param("recruitmentEndDate") LocalDate recruitmentEndDate,
			@Param("progressStartDate") LocalDate progressStartDate, @Param("progressPeriodList") List<String> progressPeriodList, Pageable pageable);
}
