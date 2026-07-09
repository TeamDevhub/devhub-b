package teamdevhub.devhub.core.report.port.out;

import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.report.domain.Report;

public interface ReportQueryRepository {

    PageResult<Report> findByReportedUser(String userGuid, PageCommand pageCommand);
    PageResult<Report> findByReporterUser(String userGuid, PageCommand pageCommand);
    PageResult<Report> findAll(PageCommand pageCommand);
}
