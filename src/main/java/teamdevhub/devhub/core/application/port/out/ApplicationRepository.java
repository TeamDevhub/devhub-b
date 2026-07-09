package teamdevhub.devhub.core.application.port.out;

import java.util.List;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface ApplicationRepository {

	void saveApplication(ProjectApplication application);

	void saveAnswers(List<ProjectApplicationAnswer> answers);

	void updateApplicationStatus(String applicationGuid, String statusCd, String approverGuid, String decisionDate);

	PageResult<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand);

	ProjectApplication findApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid);

	PageResult<ProjectApplication> findByApplicantGuid(String userGuid, PageCommand pageCommand);

	List<ProjectApplicationScore> findAcceptedByProjectGuid(String projectGuid);
}
