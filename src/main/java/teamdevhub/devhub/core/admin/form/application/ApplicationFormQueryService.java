package teamdevhub.devhub.core.admin.form.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormQueryUseCase;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormQueryRepository;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationFormQueryService implements ApplicationFormQueryUseCase {
	
	private final ApplicationFormQueryRepository applicationFormQueryRepository;

	@Override
	public PageResult<ApplicationForm> getApplicationForms(SearchApplicationFormCommand searchApplicationFormCommand,
			PageCommand pageCommand) {
		return applicationFormQueryRepository.getApplicationForms(searchApplicationFormCommand, pageCommand);
	}

	@Override
	public PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand) {
		return applicationFormQueryRepository.getApplicationFormsWithoutItem(searchApplicationFormCommand);
	}

}
