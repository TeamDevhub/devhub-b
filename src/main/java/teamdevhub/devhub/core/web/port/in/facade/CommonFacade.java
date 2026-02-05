package teamdevhub.devhub.core.web.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.api.web.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.core.admin.code.port.in.usecase.CommonCodeUseCase;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonFacade {

	private final CommonCodeUseCase commonCodeUseCase;
	
	public DataApiResponseDto<Map<String, CommonCodeResponseDto>> getCommonCodeList() {

		List<CommonCode> commonCodeList = commonCodeUseCase.getCommonCodeList();
		Map<String, CommonCodeResponseDto> result = commonCodeList.stream()
				.map(CommonCodeResponseDto::fromDomain)
				.collect(Collectors.toMap(
						CommonCodeResponseDto::getCode,
						dto -> dto
				));

		return DataApiResponseDto.successWithData(
				SuccessCode.READ_SUCCESS,
				result
		);
	}

}
