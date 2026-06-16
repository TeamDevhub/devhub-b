package teamdevhub.devhub.core.application.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.core.application.port.in.command.CreateApplicationCommand;
import teamdevhub.devhub.core.application.port.in.command.SearchAdminProjectApplicationCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface AdminProjectApplicationUseCase {
	
	PageResult<ProjectApplication> getApplicationsByProjectGuid(SearchAdminProjectApplicationCommand searchAdminProjectApplicationCommand, PageCommand pageCommand);

	ProjectApplication getApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> getAnswersByApplicationGuid(String applicationGuid);
}
