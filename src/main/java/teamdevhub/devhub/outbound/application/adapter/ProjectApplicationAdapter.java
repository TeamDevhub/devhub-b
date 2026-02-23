package teamdevhub.devhub.outbound.application.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.application.port.out.ApplicationRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.outbound.application.persistence.ProjectApplicationQueryDao;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectApplicationAdapter implements ApplicationRepository {

	private final ProjectApplicationQueryDao projectApplicationQueryDao;

	@Override
	public PageResult<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand) {
		Page<ProjectApplication> page = projectApplicationQueryDao.findApplicationsByProjectGuid(
			projectGuid,
			PageRequest.of(pageCommand.page(), pageCommand.size())
		);

		return PageResult.of(
			page.getContent(),
			page.getNumber(),
			page.getSize(),
			page.getTotalElements()
		);
	}

	@Override
	public ProjectApplication findApplicationByGuid(String applicationGuid) {
		return projectApplicationQueryDao.findApplicationByGuid(applicationGuid);
	}

	@Override
	public List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid) {
		return projectApplicationQueryDao.findAnswersByApplicationGuid(applicationGuid);
	}
}
