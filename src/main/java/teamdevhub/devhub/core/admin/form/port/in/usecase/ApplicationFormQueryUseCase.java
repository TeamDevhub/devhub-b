package teamdevhub.devhub.core.admin.form.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface ApplicationFormQueryUseCase {

	PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

	List<ApplicationForm> getApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand);

}
