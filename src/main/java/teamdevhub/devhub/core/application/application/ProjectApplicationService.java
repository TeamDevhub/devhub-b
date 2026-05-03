package teamdevhub.devhub.core.application.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.application.domain.ProjectApplication;
import teamdevhub.devhub.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.core.application.port.in.command.CreateApplicationCommand;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.core.application.port.out.ApplicationRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.shared.enums.ProjectApprovalStatus;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectApplicationService implements ProjectApplicationQueryUseCase, ProjectApplicationUseCase {

	private final ApplicationRepository applicationRepository;
	private final IdentifierProvider identifierProvider;

	@Override
	@Transactional
	public void createApplication(CreateApplicationCommand command) {
		String applicationGuid = identifierProvider.generateIdentifier();

		ProjectApplication application = ProjectApplication.builder()
			.applicationGuid(applicationGuid)
			.requirementGuid(command.requirementGuid())
			.applicantGuid(command.applicantGuid())
			.statusCd(ProjectApprovalStatus.PENDING.getCode())
			.isCanceled(false)
			.build();

		applicationRepository.saveApplication(application);

		List<ProjectApplicationAnswer> answers = command.answers().stream()
			.map(answer -> ProjectApplicationAnswer.builder()
				.projectApplicationFormGuid(answer.projectApplicationFormGuid())
				.applicationAnswerGuid(identifierProvider.generateIdentifier())
				.applicationGuid(applicationGuid)
				.applicationFormGuid(answer.applicationFormGuid())
				.projectGuid(command.projectGuid())
				.content(answer.content())
				.fileGuid(answer.fileGuid())
				.build()
			)
			.toList();

		applicationRepository.saveAnswers(answers);
	}

	@Override
	@Transactional
	public void approveApplication(ApproveApplicationCommand command) {
		String decisionDate = LocalDate.now().toString();
		applicationRepository.updateApplicationStatus(
			command.applicationGuid(),
			command.resolveStatusCd(),
			command.approverGuid(),
			decisionDate
		);
	}

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

	@Override
	public PageResult<ProjectApplication> findByApplicantGuid(String userGuid, PageCommand pageCommand) {
		return applicationRepository.findByApplicantGuid(userGuid, pageCommand);
	}

	@Override
	public List<ProjectApplication> findAcceptedByProjectGuid(String projectGuid) {
		return applicationRepository.findAcceptedByProjectGuid(projectGuid);
	}
}
