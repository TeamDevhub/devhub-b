package teamdevhub.devhub.core.application.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.core.application.port.in.command.CreateApplicationCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface ProjectApplicationUseCase {

	void createApplication(CreateApplicationCommand command);

	void approveApplication(ApproveApplicationCommand command);

	void cancelApplication(String applicationGuid, String applicantGuid);

	PageResult<ProjectApplication> findByApplicantGuid(String userGuid, PageCommand pageCommand);

	List<ProjectApplicationScore> findAcceptedByProjectGuid(String projectGuid);
}
