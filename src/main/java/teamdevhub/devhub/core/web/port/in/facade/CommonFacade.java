package teamdevhub.devhub.core.web.port.in.facade;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.web.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.admin.code.domain.CommonCode;
import teamdevhub.devhub.core.admin.code.port.in.usecase.CommonCodeUseCase;
import teamdevhub.devhub.shared.enums.SuccessCode;

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
						CommonCodeResponseDto::getCode, // Key 추출
						dto -> dto                        // Value 추출 (객체 자신)
				));

		return DataApiResponseDto.successWithData(
				SuccessCode.READ_SUCCESS,
				result
		);
	}

}
