package teamdevhub.devhub.fake.pure.application.port.out.admin;

import java.util.ArrayList;
import java.util.List;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormRepository;

public class FakeApplicationFormRepository implements ApplicationFormRepository {
	
	private final List<ApplicationForm> store = new ArrayList<>();
	
	public ApplicationForm findByApplicationFormGuid(String applicationFormGuid) {
		return store.stream()
				.filter(form -> applicationFormGuid.equals(form.getApplicationFormGuid()))
				.findAny()
				.get();
	}

	@Override
	public void save(ApplicationForm applicationForm) {
		store.add(applicationForm);
	}

}
