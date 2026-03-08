package teamdevhub.devhub.core.admin.form.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.core.admin.form.domain.ApplicationFormItem;

public interface ApplicationFormItemRepository {

	void saveAll(Set<ApplicationFormItem> items);

	void deleteByApplicationFormGuid(List<String> applicationFormGuids);

}
