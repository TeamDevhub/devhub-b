package teamdevhub.devhub.outbound.board.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.board.adapter.entity.ReportEntity;

public interface JpaReportRepository extends JpaRepository<ReportEntity, String> {

    Page<ReportEntity> findByReportedUser(String reportedUser, Pageable pageable);
    Page<ReportEntity> findByReporterUser(String reporterUser, Pageable pageable);
}
