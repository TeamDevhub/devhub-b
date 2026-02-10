package teamdevhub.devhub.outbound.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationFormEntity;

public interface JpaProjectApplicationFormRepository extends JpaRepository<ProjectApplicationFormEntity, String> {

}
