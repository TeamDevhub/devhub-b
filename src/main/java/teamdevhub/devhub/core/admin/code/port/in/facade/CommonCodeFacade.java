package teamdevhub.devhub.core.admin.code.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.api.admin.code.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.admin.code.port.in.usecase.CommonCodeUseCase;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonCodeFacade {

    private final CommonCodeUseCase commonCodeUseCase;

    public DataListApiResponseDto<CommonCodeResponseDto> getCommonCodeList() {
        List<CommonCodeResponseDto> returnData = commonCodeUseCase.getCommonCodeList().stream()
                .map(CommonCodeResponseDto::fromDomain)
                .toList();

        return DataListApiResponseDto.successWithDataList(
                SuccessCode.READ_SUCCESS,
                returnData,
                null
        );
    }
}
