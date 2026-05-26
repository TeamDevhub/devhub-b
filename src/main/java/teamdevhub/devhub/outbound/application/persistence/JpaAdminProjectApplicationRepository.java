package teamdevhub.devhub.outbound.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import teamdevhub.devhub.core.application.port.in.command.SearchAdminProjectApplicationCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationEntity;

public interface JpaAdminProjectApplicationRepository extends JpaRepository<ProjectApplicationEntity, String> {

	@Query("""
			select T1.applicantGuid
			from ProjectApplicationEntity T1
				   
			""")
	PageResult<ProjectApplicationEntity> getApplicationsByProjectGuid(
			SearchAdminProjectApplicationCommand searchAdminProjectApplicationCommand, PageCommand pageCommand);

}
