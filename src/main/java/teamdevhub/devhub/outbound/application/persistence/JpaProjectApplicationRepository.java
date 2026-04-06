package teamdevhub.devhub.outbound.application.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationEntity;

import java.util.List;
import java.util.Optional;

public interface JpaProjectApplicationRepository extends JpaRepository<ProjectApplicationEntity, String> {

	Page<ProjectApplicationEntity> findByRequirementGuidIn(List<String> requirementGuids, Pageable pageable);

	Optional<ProjectApplicationEntity> findByApplicationGuid(String applicationGuid);

	Page<ProjectApplicationEntity> findByApplicantGuid(String userGuid, PageRequest of);
}
