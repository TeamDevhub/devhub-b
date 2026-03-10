package teamdevhub.devhub.core.admin.form.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.in.command.ApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;

public interface ApplicationFormUseCase {

	List<String> saveApplicationForms(List<CreateApplicationFormCommand> additionalFormList);

	void deleteApplicationForms(List<String> deleteApplicationFormGuids);

	List<ApplicationForm> getNoCustomizedFormById(List<String> formList);

	List<ApplicationFormCommand> getCustomizedFormById(List<String> formList);

}
