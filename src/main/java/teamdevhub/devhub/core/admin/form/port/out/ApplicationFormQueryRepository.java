package teamdevhub.devhub.core.admin.form.port.out;

import java.util.List;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface ApplicationFormQueryRepository {

	PageResult<ApplicationForm> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

	List<ApplicationForm> getApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand);

}
