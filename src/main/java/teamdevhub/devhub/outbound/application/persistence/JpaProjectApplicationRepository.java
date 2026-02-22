package teamdevhub.devhub.outbound.application.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationEntity;

public interface JpaProjectApplicationRepository extends JpaRepository<ProjectApplicationEntity, String> {

	Page<ProjectApplicationEntity> findByRequirementGuid(String requirementGuid, Pageable pageable);
}
