package teamdevhub.devhub.core.admin.form.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;

public interface ApplicationFormUseCase {

	List<ApplicationFormEntity> saveApplicationForms(List<CreateApplicationFormCommand> additionalFormList);

}
