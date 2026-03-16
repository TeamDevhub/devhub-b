package teamdevhub.devhub.core.project.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.application.domain.ProjectApplicationForm;

public interface ProjectApplicationFormUseCase {

	List<String> getByProjectGuid(String projectGuid);

	List<ProjectApplicationForm> findByProjectGuid(String projectGuid);

}
