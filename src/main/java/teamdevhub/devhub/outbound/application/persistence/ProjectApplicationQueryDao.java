package teamdevhub.devhub.outbound.application.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.application.domain.ProjectApplicationScore;

import java.util.List;

public interface ProjectApplicationQueryDao {

	Page<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, Pageable pageable);

	ProjectApplication findApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid);

	List<ProjectApplicationScore> findAcceptedByProjectGuid(String projectGuid);
}
