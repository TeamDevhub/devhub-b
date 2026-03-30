package teamdevhub.devhub.outbound.admin.banner.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.outbound.admin.banner.adapter.entity.BannerEntity;

import java.time.LocalDateTime;

public interface JpaBannerRepository extends JpaRepository<BannerEntity, String> {

        @Query("""
                select B from BannerEntity B
                where (:keyword IS NULL OR B.title LIKE %:keyword% OR B.description LIKE %:keyword%)
                AND (:isUsed IS NULL OR B.used = :isUsed)
                AND (:isMainBanner IS NULL OR B.isMainBanner = :isMainBanner)
                AND (:alwaysPublication IS NULL OR (
                        (:alwaysPublication = true AND B.publicationEndDate = :maxDate)
                        OR 
                        (:alwaysPublication = false AND (B.publicationEndDate IS NULL OR B.publicationEndDate <> :maxDate))
                ))
                AND (:publicationStartDate IS NULL OR B.publicationStartDate <= :publicationStartDate)
                AND (:publicationEndDate IS NULL OR B.publicationEndDate >= :publicationEndDate)
        """)
        Page<BannerEntity> findByComplexCondition(
                @Param("keyword") String keyword,
                @Param("isUsed") Boolean isUsed,
                @Param("isMainBanner") Boolean isMainBanner,
                @Param("alwaysPublication") Boolean alwaysPublication,
                @Param("publicationStartDate") LocalDateTime publicationStartDate,
                @Param("publicationEndDate") LocalDateTime publicationEndDate,
                @Param("maxDate") LocalDateTime maxDate,
                Pageable pageable
        );
}
