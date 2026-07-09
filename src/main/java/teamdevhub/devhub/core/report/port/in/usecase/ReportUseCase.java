package teamdevhub.devhub.core.report.port.in.usecase;

import teamdevhub.devhub.core.report.port.in.command.CreateReportCommand;

public interface ReportUseCase {

    void createReport(CreateReportCommand createReportCommand);
}
