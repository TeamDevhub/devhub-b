package teamdevhub.devhub.outbound.report.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.core.report.domain.Report;
import teamdevhub.devhub.core.report.port.out.ReportRepository;
import teamdevhub.devhub.outbound.report.adapter.mapper.ReportMapper;
import teamdevhub.devhub.outbound.report.persistence.JpaReportRepository;

@Component
@RequiredArgsConstructor
public class ReportAdapter implements ReportRepository {

    private final JpaReportRepository jpaReportRepository;

    @Override
    public boolean existsDuplicate(String reporterUser, String boardGuid, String commentGuid) {
        return jpaReportRepository.existsDuplicate(reporterUser, boardGuid, commentGuid);
    }

    @Override
    public void save(Report report) {
        jpaReportRepository.save(ReportMapper.toEntity(report));
    }
}
