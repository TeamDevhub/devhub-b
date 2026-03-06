package teamdevhub.devhub.core.admin.form.port.out;


import java.util.List;

import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;

public interface ApplicationFormRepository {

	void save(ApplicationForm applicationForm);
	
	ApplicationForm findByApplicationFormGuid(String applicationFormGuid);

	List<ApplicationForm> findByProjectGuidAndIsCustomized(String projectGuid);

}
