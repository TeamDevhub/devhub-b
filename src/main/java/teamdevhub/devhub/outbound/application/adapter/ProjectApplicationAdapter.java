package teamdevhub.devhub.outbound.application.adapter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.core.application.port.out.ApplicationRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationAnswerEntity;
import teamdevhub.devhub.outbound.application.adapter.entity.ProjectApplicationEntity;
import teamdevhub.devhub.outbound.application.adapter.mapper.ApplicationMapper;
import teamdevhub.devhub.outbound.application.persistence.JpaProjectApplicationAnswerRepository;
import teamdevhub.devhub.outbound.application.persistence.JpaProjectApplicationRepository;
import teamdevhub.devhub.outbound.application.persistence.ProjectApplicationQueryDao;

@Component
@RequiredArgsConstructor
public class ProjectApplicationAdapter implements ApplicationRepository {

	private final ProjectApplicationQueryDao projectApplicationQueryDao;
	private final JpaProjectApplicationRepository jpaProjectApplicationRepository;
	private final JpaProjectApplicationAnswerRepository jpaProjectApplicationAnswerRepository;

	@Override
	public void saveApplication(ProjectApplication application) {
		jpaProjectApplicationRepository.save(ApplicationMapper.toApplicationEntity(application));
	}

	@Override
	public void saveAnswers(List<ProjectApplicationAnswer> answers) {
		List<ProjectApplicationAnswerEntity> entities = answers.stream()
			.map(ApplicationMapper::toAnswerEntity)
			.toList();
		jpaProjectApplicationAnswerRepository.saveAll(entities);
	}

	@Override
	public void updateApplicationStatus(String applicationGuid, String statusCd, String approverGuid, String decisionDate) {
		ProjectApplicationEntity entity = jpaProjectApplicationRepository
			.findByApplicationGuid(applicationGuid)
			.orElseThrow(() -> new IllegalArgumentException("지원 정보를 찾을 수 없습니다."));
		entity.updateStatus(statusCd, approverGuid, decisionDate);
		jpaProjectApplicationRepository.save(entity);
	}

	@Override
	public PageResult<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand) {
		Page<ProjectApplication> page = projectApplicationQueryDao.findApplicationsByProjectGuid(
			projectGuid,
			PageRequest.of(pageCommand.page(), pageCommand.size())
		);
		return PageResult.of(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements());
	}

	@Override
	public ProjectApplication findApplicationByGuid(String applicationGuid) {
		return projectApplicationQueryDao.findApplicationByGuid(applicationGuid);
	}

	@Override
	public List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid) {
		return projectApplicationQueryDao.findAnswersByApplicationGuid(applicationGuid);
	}

	@Override
	public PageResult<ProjectApplication> findByApplicantGuid(String userGuid, PageCommand pageCommand) {
		Page<ProjectApplicationEntity> page = jpaProjectApplicationRepository.findByApplicantGuid(
				userGuid,
				PageRequest.of(pageCommand.page(), pageCommand.size())
			);
		
		return PageResult.of(
				page.getContent().stream().map(entity -> ApplicationMapper.toApplicationOnly(entity)).toList(),
				page.getNumber(),
				page.getSize(),
				page.getTotalElements());
	}

	@Override
	public List<ProjectApplicationScore> findAcceptedByProjectGuid(String projectGuid) {
		return projectApplicationQueryDao.findAcceptedByProjectGuid(projectGuid);
	}
}
