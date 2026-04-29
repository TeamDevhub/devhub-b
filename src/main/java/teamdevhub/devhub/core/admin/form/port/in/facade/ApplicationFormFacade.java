package teamdevhub.devhub.core.admin.form.port.in.facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.command.UpdateApplicationFormCommand;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormQueryUseCase;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.shared.enums.SuccessCode;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationFormFacade {

	private final ApplicationFormUseCase applicationFormUseCase;
	private final ApplicationFormQueryUseCase applicationFormQueryUseCase;

	public DataListApiResponseDto<ApplicationFormListItemDto> getApplicationForms(
			SearchApplicationFormCommand command, PageCommand pageCommand) {
		PageResult<ApplicationForm> result = applicationFormQueryUseCase.getApplicationForms(command, pageCommand);
		List<ApplicationFormListItemDto> dataList = result.content().stream()
				.map(ApplicationFormListItemDto::fromDomain)
				.toList();
		return DataListApiResponseDto.successWithDataList(
				SuccessCode.READ_SUCCESS,
				dataList,
				PageResponseDto.from(result)
		);
	}

	public DataApiResponseDto<Void> createApplicationForm(CreateApplicationFormCommand command) {
		applicationFormUseCase.saveApplicationForms(List.of(command));
		return DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS);
	}

	public DataApiResponseDto<Void> updateApplicationForm(String applicationFormGuid, UpdateApplicationFormCommand command) {
		applicationFormUseCase.updateApplicationForm(applicationFormGuid, command);
		return DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS);
	}

	public record ApplicationFormListItemDto(
			String applicationFormGuid,
			String typeCd,
			String title,
			String helpText,
			boolean isCustomized,
			boolean isUsed
	) {
		public static ApplicationFormListItemDto fromDomain(ApplicationForm form) {
			return new ApplicationFormListItemDto(
					form.getApplicationFormGuid(),
					form.getTypeCd(),
					form.getTitle(),
					form.getHelpText(),
					form.isCustomized(),
					form.isUsed()
			);
		}
	}
}
