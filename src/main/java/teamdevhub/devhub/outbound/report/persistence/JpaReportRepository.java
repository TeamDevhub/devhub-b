package teamdevhub.devhub.outbound.report.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.outbound.report.adapter.entity.ReportEntity;

public interface JpaReportRepository extends JpaRepository<ReportEntity, String> {

    Page<ReportEntity> findByReportedUser(String reportedUser, Pageable pageable);
    Page<ReportEntity> findByReporterUser(String reporterUser, Pageable pageable);

    @Query("""
            select case when count(r) > 0 then true else false end
            from ReportEntity r
            where r.reporterUser = :reporterUser
              and (
                    (:boardGuid is not null and r.boardGuid = :boardGuid)
                 or (:commentGuid is not null and r.commentGuid = :commentGuid)
              )
            """)
    boolean existsDuplicate(
            @Param("reporterUser") String reporterUser,
            @Param("boardGuid") String boardGuid,
            @Param("commentGuid") String commentGuid
    );
}
