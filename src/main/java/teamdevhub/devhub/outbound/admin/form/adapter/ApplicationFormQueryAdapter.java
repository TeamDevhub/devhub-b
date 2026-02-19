package teamdevhub.devhub.outbound.admin.form.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormQueryRepository;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.outbound.admin.form.persistence.ApplicationFormQueryDao;
@Component
@RequiredArgsConstructor
public class ApplicationFormQueryAdapter implements ApplicationFormQueryRepository {
	
	private final ApplicationFormQueryDao applicationFormQueryDao;

	@Override
	public PageResult<ApplicationForm> getApplicationForms(SearchApplicationFormCommand searchApplicationFormCommand,
			PageCommand pageCommand) {
		Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size(), Sort.by("registeredDate").descending());
		
		Page<ApplicationForm> pagedApplicationEntityList = applicationFormQueryDao.listApplicationForm(searchApplicationFormCommand, pageable);
		return null;
	}

	@Override
	public PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand) {
		Page<ApplicationForm> pagedApplicationForm = applicationFormQueryDao.listApplicationFormWithoutItem(searchApplicationFormCommand);
		
		List<ApplicationForm> appplicationFormList = pagedApplicationForm.getContent();
		return PageResult.of(
				appplicationFormList,
				pagedApplicationForm.getNumber(),
				pagedApplicationForm.getSize(),
				pagedApplicationForm.getTotalElements());
	}

}
