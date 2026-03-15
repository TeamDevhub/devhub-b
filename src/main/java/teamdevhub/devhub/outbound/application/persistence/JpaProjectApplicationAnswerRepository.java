package teamdevhub.devhub.outbound.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationAnswerEntity;

import java.util.List;

public interface JpaProjectApplicationAnswerRepository extends JpaRepository<ProjectApplicationAnswerEntity, String> {

	List<ProjectApplicationAnswerEntity> findByApplicationGuid(String applicationGuid);
}
