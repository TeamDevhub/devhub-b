package teamdevhub.devhub.outbound.admin.form.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormRepository;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;
import teamdevhub.devhub.outbound.admin.form.adapter.mapper.ApplicationFormMapper;
import teamdevhub.devhub.outbound.admin.form.persistence.JpaApplicationFormRepository;

@Component
@RequiredArgsConstructor
public class ApplicationFormAdapter implements ApplicationFormRepository{
	
	private JpaApplicationFormRepository jpaApplicationFormRepository;

	@Override
	public void save(ApplicationForm applicationForm) {
		ApplicationFormEntity applicationFormEntity = ApplicationFormMapper.toEntity(applicationForm);
		jpaApplicationFormRepository.save(applicationFormEntity);
	}

	@Override
	public ApplicationForm findByApplicationFormGuid(String applicationFormGuid) {
		ApplicationFormEntity applicationFormEntity = jpaApplicationFormRepository.findByApplicationFormGuid(applicationFormGuid);
		return ApplicationFormMapper.toApplicationForm(applicationFormEntity);
	}

}
