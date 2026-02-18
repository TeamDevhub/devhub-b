package teamdevhub.devhub.outbound.admin.form.adapter;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormRepository;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;
import teamdevhub.devhub.outbound.admin.form.adapter.mapper.ApplicationFormMapper;
import teamdevhub.devhub.outbound.admin.form.persistence.JpaApplicationFormRepository;

@Component
@RequiredArgsConstructor
public class ApplicationFormAdapter implements ApplicationFormRepository{
	
	private JpaApplicationFormRepository jpaApplicationFormRepository;

	@Override
	public List<ApplicationFormEntity> saveAll(Set<ApplicationForm> applicationForms) {
		List<ApplicationFormEntity> entityList = applicationForms.stream()
				.map(ApplicationFormMapper::toEntity)
				.toList();
		return jpaApplicationFormRepository.saveAll(entityList);
	}

}
