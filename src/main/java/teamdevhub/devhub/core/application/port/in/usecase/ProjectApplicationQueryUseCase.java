package teamdevhub.devhub.core.application.port.in.usecase;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface ProjectApplicationQueryUseCase {

	PageResult<ProjectApplication> getApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand);
}
