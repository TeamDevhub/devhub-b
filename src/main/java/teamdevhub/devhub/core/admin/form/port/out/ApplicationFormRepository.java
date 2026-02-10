package teamdevhub.devhub.core.admin.form.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;

public interface ApplicationFormRepository {

	List<ApplicationFormEntity> saveAll(Set<ApplicationForm> applicationForms);
}
