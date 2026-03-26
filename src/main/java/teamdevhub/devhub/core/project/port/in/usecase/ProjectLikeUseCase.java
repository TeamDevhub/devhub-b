package teamdevhub.devhub.core.project.port.in.usecase;

import teamdevhub.devhub.core.project.domain.ProjectLike;
import teamdevhub.devhub.core.project.domain.vo.command.CreateProjectLikeCommand;

public interface ProjectLikeUseCase {

	void toggleProjectLike(CreateProjectLikeCommand createProjectLikeCommand);

	ProjectLike findByProjectGuidAndUserGuid(String projectGuid, String userGuid);

}
