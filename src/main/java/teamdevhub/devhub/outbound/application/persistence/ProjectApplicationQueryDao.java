package teamdevhub.devhub.outbound.application.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamdevhub.devhub.core.application.domain.ProjectApplication;

public interface ProjectApplicationQueryDao {

	Page<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, Pageable pageable);
}
