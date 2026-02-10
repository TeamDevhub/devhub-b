package teamdevhub.devhub.core.admin.form.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.domain.ApplicationFormItem;
import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormItemRepository;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormRepository;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;
import teamdevhub.devhub.outbound.admin.form.adapter.entity.ApplicationFormEntity;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationFormService implements ApplicationFormUseCase{
	
	private final IdentifierProvider identifierProvider;
	private final ApplicationFormRepository applicationFormRepository;
	private final ApplicationFormItemRepository applicationFormItemRepository;
	
	@Override
	public List<ApplicationFormEntity> saveApplicationForms(List<CreateApplicationFormCommand> applitionalFormCommandList) {
		Set<ApplicationForm> applicationForms = applitionalFormCommandList.stream()
				.map(applicationFormCommand -> {
				ApplicationForm applicationForm = createApplicationForm(applicationFormCommand);
				saveApplcationFormItems(applicationForm.getApplicationFormGuid(), applicationFormCommand.getItemList());
				return applicationForm;
				})
				.collect(Collectors.toUnmodifiableSet());
		return applicationFormRepository.saveAll(applicationForms);
	}
	
	private ApplicationForm createApplicationForm(CreateApplicationFormCommand createApplicationFormCommand) {
		String applicationFormGuid = identifierProvider.generateIdentifier();
		return ApplicationForm.createCustomApplicationForm(createApplicationFormCommand, applicationFormGuid);
	}

	private void saveApplcationFormItems(String applicationFormGuid, List<String> itemList) {
		Set<ApplicationFormItem> items = itemList.stream()
				.map(item -> {
					String applicationFormItemGuid = identifierProvider.generateIdentifier();
					return ApplicationFormItem.createApplicationFormItem(applicationFormItemGuid, applicationFormGuid, item);
				})
				.collect(Collectors.toUnmodifiableSet());
		applicationFormItemRepository.saveAll(items);
		
	}
}
