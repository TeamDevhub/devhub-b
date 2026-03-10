package teamdevhub.devhub.core.admin.form.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.domain.ApplicationFormItem;
import teamdevhub.devhub.core.admin.form.port.in.command.ApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormItemRepository;
import teamdevhub.devhub.core.admin.form.port.out.ApplicationFormRepository;
import teamdevhub.devhub.core.common.provider.IdentifierProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationFormService implements ApplicationFormUseCase{
	
	private final IdentifierProvider identifierProvider;
	private final ApplicationFormRepository applicationFormRepository;
	private final ApplicationFormItemRepository applicationFormItemRepository;
	
	@Override
	public List<String> saveApplicationForms(List<CreateApplicationFormCommand> applitionalFormCommandList) {
		List<String> applicationFormGuids = new ArrayList<>();
		applitionalFormCommandList.stream()
				.forEach(applicationFormCommand -> {
				ApplicationForm applicationForm = createApplicationForm(applicationFormCommand);
				saveApplicationForm(applicationForm);
				if(applicationFormCommand.getItemList() != null && applicationFormCommand.getItemList().size() > 0) {
					saveApplcationFormItems(applicationForm.getApplicationFormGuid(), applicationFormCommand.getItemList());
				}
				applicationFormGuids.add(applicationForm.getApplicationFormGuid());
				});
		return applicationFormGuids;
	}
	
	private ApplicationForm createApplicationForm(CreateApplicationFormCommand createApplicationFormCommand) {
		String applicationFormGuid = identifierProvider.generateIdentifier();
		return ApplicationForm.createCustomApplicationForm(createApplicationFormCommand, applicationFormGuid);
	}
	
	private void saveApplicationForm(ApplicationForm applicationForm) {
		applicationFormRepository.save(applicationForm);
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

	@Override
	public void deleteApplicationForms(List<String> deleteApplicationFormGuids) {
		List<ApplicationForm> applicationFormList = applicationFormRepository.findByIdAndIsCustomized(deleteApplicationFormGuids);
		List<String> applicationFormGuids = applicationFormList.stream()
												.map(ApplicationForm::getApplicationFormGuid)
												.toList();
		applicationFormItemRepository.deleteByApplicationFormGuid(applicationFormGuids);
		applicationFormRepository.deleteByApplicationFormGuid(applicationFormGuids);
		
	}

	@Override
	public List<ApplicationForm> getNoCustomizedFormById(List<String> formList) {
		return applicationFormRepository.findByIdAndIsNotCustomized(formList);
	}

	@Override
	public List<ApplicationFormCommand> getCustomizedFormById(List<String> formList) {
		List<ApplicationForm> applicationFormList = applicationFormRepository.findByIdAndIsCustomized(formList);
		List<ApplicationFormCommand> commandList = applicationFormList.stream()
				.map(form -> {
					List<ApplicationFormItem> itemList = applicationFormItemRepository.findByFormGuid(form.getApplicationFormGuid());
					return ApplicationFormCommand.fromDomain(form, itemList);
					})
				.toList();
		return commandList;
	}

}
