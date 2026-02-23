package teamdevhub.devhub.core.application.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.core.application.port.out.ApplicationRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectApplicationService implements ProjectApplicationQueryUseCase {

	private final ApplicationRepository applicationRepository;

	@Override
	public PageResult<ProjectApplication> getApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand) {
		return applicationRepository.findApplicationsByProjectGuid(projectGuid, pageCommand);
	}

	@Override
	public ProjectApplication getApplicationByGuid(String applicationGuid) {
		return applicationRepository.findApplicationByGuid(applicationGuid);
	}

	@Override
	public List<ProjectApplicationAnswer> getAnswersByApplicationGuid(String applicationGuid) {
		return applicationRepository.findAnswersByApplicationGuid(applicationGuid);
	}
}
