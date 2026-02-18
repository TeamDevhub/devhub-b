package teamdevhub.devhub.core.admin.form.port.in.usecase;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface ApplicationFormQueryUseCase {

	PageResult<ApplicationForm> getApplicationForms(SearchApplicationFormCommand searchApplicationFormCommand,
			PageCommand pageCommand);

	PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

}
