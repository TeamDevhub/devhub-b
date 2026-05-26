package teamdevhub.devhub.core.application.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.application.port.in.command.SearchAdminProjectApplicationCommand;
import teamdevhub.devhub.core.application.port.in.usecase.AdminProjectApplicationUseCase;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationEntity;
import teamdevhub.devhub.outbound.application.persistence.JpaAdminProjectApplicationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProjectApplicationService implements AdminProjectApplicationUseCase {
	
	private final JpaAdminProjectApplicationRepository jpaAdminProjectApplicationRepository;

	@Override
	public List<ProjectApplicationAnswer> getAnswersByApplicationGuid(String applicationGuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<ProjectApplication> getApplicationsByProjectGuid(
			SearchAdminProjectApplicationCommand searchAdminProjectApplicationCommand, PageCommand pageCommand) {
		PageResult<ProjectApplicationEntity> entityResult = jpaAdminProjectApplicationRepository.getApplicationsByProjectGuid(searchAdminProjectApplicationCommand, pageCommand);
		return null;
	}

	@Override
	public ProjectApplication getApplicationByGuid(String applicationGuid) {
		// TODO Auto-generated method stub
		return null;
	}



}
