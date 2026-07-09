package teamdevhub.devhub.core.application.port.in.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.core.admin.form.port.in.usecase.ApplicationFormQueryUseCase;
import teamdevhub.devhub.core.application.port.in.command.SearchApplicationFormCommand;
import teamdevhub.devhub.core.application.port.in.facade.model.ApplicationFormResponseDto;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.shared.enums.SuccessCode;

@Service("applicationApplicationFormFacade")
@RequiredArgsConstructor
public class ApplicationFormFacade {
	
	private final ApplicationFormQueryUseCase applicationFormQueryUseCase;
	
	public DataListApiResponseDto<ApplicationFormResponseDto> getApplicationFormsWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand) {
		PageResult<ApplicationForm> pagedApplicationList = applicationFormQueryUseCase.getApplicationFormsWithoutItem(searchApplicationFormCommand);
		List<ApplicationFormResponseDto> applicationFormResponseDtoList = pagedApplicationList.content().stream()
				.map(ApplicationFormResponseDto::fromDomain)
				.toList();
		
		return DataListApiResponseDto.successWithDataList(
				SuccessCode.READ_SUCCESS,
				applicationFormResponseDtoList,
				PageResponseDto.from(pagedApplicationList));
	}
}
