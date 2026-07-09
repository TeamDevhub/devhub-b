package teamdevhub.devhub.core.report.port.in.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.report.port.in.command.CreateReportCommand;
import teamdevhub.devhub.core.report.port.in.usecase.ReportUseCase;
import teamdevhub.devhub.shared.enums.SuccessCode;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportFacade {

    private final ReportUseCase reportUseCase;

    public DataApiResponseDto<Void> createReport(CreateReportCommand createReportCommand) {
        reportUseCase.createReport(createReportCommand);
        return DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS);
    }
}
