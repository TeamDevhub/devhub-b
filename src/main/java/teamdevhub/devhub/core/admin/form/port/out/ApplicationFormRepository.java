package teamdevhub.devhub.core.admin.form.port.out;


import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;

public interface ApplicationFormRepository {

	void save(ApplicationForm applicationForm);
	
	ApplicationForm findByApplicationFormGuid(String applicationFormGuid);

}
