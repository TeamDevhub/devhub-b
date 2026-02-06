package teamdevhub.devhub.outbound.project.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import teamdevhub.devhub.outbound.project.adapter.entity.ProjectRequirementEntity;

public interface JpaProjectRequirementRepository extends JpaRepository<ProjectRequirementEntity, String> {

}
