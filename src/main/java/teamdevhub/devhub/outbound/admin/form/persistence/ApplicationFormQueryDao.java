package teamdevhub.devhub.outbound.admin.form.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;

public interface ApplicationFormQueryDao {

	Page<ApplicationForm> listApplicationForm(SearchApplicationFormCommand searchApplicationFormCommand,
			Pageable pageable);

	Page<ApplicationForm> listApplicationFormWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand);

}
