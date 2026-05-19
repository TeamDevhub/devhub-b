package teamdevhub.devhub.core.report.port.in.usecase;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.report.domain.Report;

public interface ReportQueryUseCase {

    PageResult<Report> getReportsByReportedUser(String userGuid, PageCommand pageCommand);
    PageResult<Report> getReportsByReporterUser(String userGuid, PageCommand pageCommand);
    PageResult<Report> getAllReports(PageCommand pageCommand);
}
