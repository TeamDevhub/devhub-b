package teamdevhub.devhub.outbound.admin.form.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;

public interface JpaApplicationFormRepository  extends JpaRepository<ApplicationFormEntity, String>{

	ApplicationFormEntity findByApplicationFormGuid(String applicationFormGuid);

	@Query("""
			select af
			from ApplicationFormEntity af
			where af.isCustomized = true
			and (:projectGuid is null or af.projectGuid = :projectGuid)
			""")
	List<ApplicationFormEntity> findByProjectGuidAndIsCustomized(@Param("projectGuid") String projectGuid);

	@Query("""
			select af
			from ApplicationFormEntity af
			where af.isCustomized = true
			and (:deleteApplicationFormGuids is null or af.applicationFormGuid in :deleteApplicationFormGuids)
			""")
	List<ApplicationFormEntity> findByIdAndIsCustomized(@Param("deleteApplicationFormGuids") List<String> deleteApplicationFormGuids);
}
