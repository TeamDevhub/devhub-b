package teamdevhub.devhub.outbound.project.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;

import java.util.List;
import java.util.Optional;

public interface JpaProjectRequirementRepository extends JpaRepository<ProjectRequirementEntity, String> {

	List<ProjectRequirementEntity> findByProjectGuid(String projectGuid);

	Optional<ProjectRequirementEntity> findByProjectRequirementGuid(String projectRequirementGuid);
}
