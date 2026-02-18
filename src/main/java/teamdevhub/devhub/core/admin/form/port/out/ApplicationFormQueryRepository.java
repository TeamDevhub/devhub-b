package teamdevhub.devhub.core.admin.form.port.out;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface ApplicationFormQueryRepository {

	PageResult<ApplicationForm> getApplicationForms(SearchApplicationFormCommand searchApplicationFormCommand,
			PageCommand pageCommand);

	PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

}
