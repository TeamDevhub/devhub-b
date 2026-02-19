package teamdevhub.devhub.fake.pure.application.port.out.admin.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import teamdevhub.devhub.core.admin.form.domain.ApplicationFormItem;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormItemRepository;

public class FakeApplicationFormItemRepository implements ApplicationFormItemRepository {

	private final List<ApplicationFormItem> store = new ArrayList<>();
	
	@Override
	public void saveAll(Set<ApplicationFormItem> items) {
		store.addAll(items);
	}
	
	public List<ApplicationFormItem> findByFormGuid(String formGuid) {
		return store.stream()
				.filter(item -> formGuid.equals(item.getFormGuid()))
				.toList();
	}

}
