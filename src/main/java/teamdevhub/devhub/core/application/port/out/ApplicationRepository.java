package teamdevhub.devhub.core.application.port.out;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface ApplicationRepository {

	PageResult<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand);
}
