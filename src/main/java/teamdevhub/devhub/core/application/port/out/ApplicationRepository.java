package teamdevhub.devhub.core.application.port.out;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

import java.util.List;

public interface ApplicationRepository {

	void saveApplication(ProjectApplication application);

	void saveAnswers(List<ProjectApplicationAnswer> answers);

	void updateApplicationStatus(String applicationGuid, String statusCd, String approverGuid, String decisionDate);

	PageResult<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand);

	ProjectApplication findApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid);

	PageResult<ProjectApplication> findByApplicantGuid(String userGuid, PageCommand pageCommand);

	List<ProjectApplication> findAcceptedByProjectGuid(String projectGuid);
}
