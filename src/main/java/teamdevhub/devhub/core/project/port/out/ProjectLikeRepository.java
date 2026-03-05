package teamdevhub.devhub.core.project.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.core.project.domain.ProjectLike;

public interface ProjectLikeRepository {

	List<ProjectLike> findByProjectGuid(Set<String> projectGuids);

	int countByProjectGuid(String projectGuid);

}
