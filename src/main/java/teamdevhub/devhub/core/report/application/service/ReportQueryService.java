package teamdevhub.devhub.core.report.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.report.domain.Report;
import teamdevhub.devhub.core.report.port.in.usecase.ReportQueryUseCase;
import teamdevhub.devhub.core.report.port.out.ReportQueryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportQueryService implements ReportQueryUseCase {

    private final ReportQueryRepository reportQueryRepository;

    @Override
    public PageResult<Report> getReportsByReportedUser(String userGuid, PageCommand pageCommand) {
        return reportQueryRepository.findByReportedUser(userGuid, pageCommand);
    }

    @Override
    public PageResult<Report> getReportsByReporterUser(String userGuid, PageCommand pageCommand) {
        return reportQueryRepository.findByReporterUser(userGuid, pageCommand);
    }

    @Override
    public PageResult<Report> getAllReports(PageCommand pageCommand) {
        return reportQueryRepository.findAll(pageCommand);
    }
}
