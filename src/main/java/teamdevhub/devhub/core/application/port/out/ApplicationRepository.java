package teamdevhub.devhub.core.application.port.out;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

import java.util.List;

public interface ApplicationRepository {

	void saveApplication(ProjectApplication application);

	void saveAnswers(List<ProjectApplicationAnswer> answers);

	PageResult<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand);

	ProjectApplication findApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid);
}
