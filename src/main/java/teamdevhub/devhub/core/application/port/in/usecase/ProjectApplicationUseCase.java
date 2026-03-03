package teamdevhub.devhub.core.application.port.in.usecase;

import teamdevhub.devhub.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.core.application.port.in.command.CreateApplicationCommand;

public interface ProjectApplicationUseCase {

	void createApplication(CreateApplicationCommand command);

	void approveApplication(ApproveApplicationCommand command);
}
