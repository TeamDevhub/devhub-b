package teamdevhub.devhub.outbound.project.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import teamdevhub.devhub.outbound.project.adapter.entity.ProjectEntity;

public interface JpaProjectRepository  extends JpaRepository<ProjectEntity, String> {
}
