package teamdevhub.devhub.fake.pure.application.port.in.usecase.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormUseCase;

public class FakeApplicationFormUseCase implements ApplicationFormUseCase {
	
	private final Map<String, ApplicationForm> store = new HashMap<>();

	@Override
	public List<String> saveApplicationForms(List<CreateApplicationFormCommand> additionalFormList) {
		// TODO Auto-generated method stub
		return null;
	}

}
