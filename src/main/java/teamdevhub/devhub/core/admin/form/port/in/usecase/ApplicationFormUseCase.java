package teamdevhub.devhub.core.admin.form.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;

public interface ApplicationFormUseCase {

	List<String> saveApplicationForms(List<CreateApplicationFormCommand> additionalFormList);

	void deleteApplicationForms(String projectGuid);

}
