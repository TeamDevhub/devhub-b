package teamdevhub.devhub.outbound.project.persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.outbound.project.adapter.entity.ProjectEntity;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, String> {

	@Query("""
			select p
			from ProjectEntity p
			where exists (
				select 1
				from ProjectRequirementEntity pr
				where pr.projectGuid = p.projectGuid
				and (:positionCodeList is null or pr.positionCd in :positionCodeList)
				and (:positionLevelCodeList is null or pr.levelCd in :positionLevelCodeList)
				)
			and (:skillCodeList is null or exists (
				select 1
				from ProjectSkillEntity ps
				where ps.projectGuid = p.projectGuid
				and ps.skillCd in :skillCodeList
				))
			and (:keyword is null or (
		        p.title like concat('%', :keyword, '%')
		    ))
			and (:regionCodeList is null or p.progressRegionCd in :regionCodeList)
			and (:projectRecruitTypeList is null or p.recruitmentTypeCd in :projectRecruitTypeList)
			and (:projectRecruitStatusList is null or (
													   ('3201' in :projectRecruitStatusList and CURRENT_DATE between p.recruitmentStartDate and p.recruitmentEndDate)
													    or
													   ('3202' in :projectRecruitStatusList and CURRENT_DATE > p.recruitmentEndDate)
													   	or
													   ('3203' in :projectRecruitStatusList and CURRENT_DATE < p.recruitmentStartDate) ))
			and (:projectProgressTypeList is null or p.progressTypeCd in :projectProgressTypeList)
			and (:recruitmentStartDate is null or p.recruitmentStartDate >= :recruitmentStartDate)
			and (:recruitmentEndDate is null or p.recruitmentEndDate <= :recruitmentEndDate)
			and (:progressStartDate is null or p.progressStartDate >= :progressStartDate)
			order by 
				case when :order = '001' then p.registeredDate end desc,
				case when :order = '002' then (p.recruitmentEndDate - CURRENT_TIMESTAMP) end asc
			""")
	Page<ProjectEntity> findBySearchCondition(@Param("keyword") String keyword, @Param("order") String order, @Param("skillCodeList") List<String> skillCodeList,
			@Param("regionCodeList") List<String> regionCodeList, @Param("positionCodeList") List<String> positionCodeList, @Param("positionLevelCodeList") List<String> positionLevelCodeList,
			@Param("projectRecruitTypeList") List<String> projectRecruitTypeList, @Param("projectRecruitStatusList") List<String> projectRecruitStatusList,
			@Param("projectProgressTypeList") List<String> projectProgressTypeList, @Param("recruitmentStartDate") LocalDateTime recruitmentStartDate, @Param("recruitmentEndDate") LocalDateTime recruitmentEndDate,
			@Param("progressStartDate") LocalDateTime progressStartDate, @Param("progressPeriodList") List<String> progressPeriodList, Pageable pageable);

	@Modifying
	@Query("""
			update ProjectEntity p
			set p.userGuid = COALESCE(:userGuid, p.userGuid),
				p.username = COALESCE(:username, p.username),
				p.attachmentFileGuid = COALESCE(:attachmentFileGuid, p.attachmentFileGuid),
				p.imageFileGuid = COALESCE(:imageFileGuid, p.imageFileGuid),
				p.title = COALESCE(:title, p.title),
				p.content = COALESCE(:content, p.content),
				p.recruitmentStartDate = COALESCE(:recruitmentStartDate, p.recruitmentStartDate),
				p.recruitmentEndDate = COALESCE(:recruitmentEndDate, p.recruitmentEndDate),
				p.progressStartDate = COALESCE(:progressStartDate, p.progressStartDate),
				p.progressEndDate = COALESCE(:progressEndDate, p.progressEndDate),
				p.recruitmentTypeCd = COALESCE(:recruitmentTypeCd, p.recruitmentTypeCd),
				p.progressRegionCd = COALESCE(:progressRegionCd, p.progressRegionCd),
				p.progressTypeCd = COALESCE(:progressTypeCd, p.progressTypeCd),
				p.category = COALESCE(:category, p.category)
			where p.projectGuid = :projectGuid
			""")
	void update(@Param("projectGuid") String projectGuid, @Param("userGuid") String userGuid, @Param("username") String username, @Param("attachmentFileGuid") String attachmentFileGuid, @Param("imageFileGuid") String imageFileGuid,
			@Param("title") String title, @Param("content") String content, @Param("recruitmentStartDate") LocalDate recruitmentStartDate, @Param("recruitmentEndDate") LocalDate recruitmentEndDate,
			@Param("progressStartDate") LocalDate progressStartDate, @Param("progressEndDate") LocalDate progressEndDate, @Param("recruitmentTypeCd") String recruitmentTypeCd, @Param("progressRegionCd") String progressRegionCd,
			@Param("progressTypeCd") String progressTypeCd, @Param("category") String category);


}
