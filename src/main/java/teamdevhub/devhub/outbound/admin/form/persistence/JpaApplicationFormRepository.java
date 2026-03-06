package teamdevhub.devhub.outbound.admin.form.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;

public interface JpaApplicationFormRepository  extends JpaRepository<ApplicationFormEntity, String>{

	ApplicationFormEntity findByApplicationFormGuid(String applicationFormGuid);

	@Query("""
			select af
			from applicationFormEntity af
			where isCustomized = true
			(:projectGuid is null or af.projectGuid = :projectGuid)
			""")
	List<ApplicationFormEntity> findByProjectGuidAndIsCustomized(String projectGuid);
}
