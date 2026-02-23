package teamdevhub.devhub.core.application.port.in.usecase;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

import java.util.List;

public interface ProjectApplicationQueryUseCase {

	PageResult<ProjectApplication> getApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand);

	ProjectApplication getApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> getAnswersByApplicationGuid(String applicationGuid);
}
