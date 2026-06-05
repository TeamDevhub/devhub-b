package teamdevhub.devhub.core.application.port.out;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.port.in.command.SearchAdminProjectApplicationCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface AdminProjectApplicationRepository {

	PageResult<ProjectApplication> getApplicationsByProjectGuid(
			SearchAdminProjectApplicationCommand searchAdminProjectApplicationCommand, PageCommand pageCommand);

}
