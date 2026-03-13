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
            AND B.used = :isUsed
            AND B.isMainBanner = :isMainBanner
            AND(
                (:alwaysPublication = true AND B.publicationEndDate = :maxDate)
                OR
                (:alwaysPublication = false AND B.publicationStartDate <= :publicationStartDate AND B.publicationEndDate >= :publicationEndDate)
            )
            """)
    Page<BannerEntity> findByComplexCondition(
            @Param("keyword") String keyword,
            @Param("isUsed") String isUsed,
            @Param("isMainBanner") String isMainBanner,
            @Param("alwaysPublication") boolean alwaysPublication,
            @Param("publicationStartDate") String publicationStartDate,
            @Param("publicationEndDate") String publicationEndDate,
            @Param("maxDate") LocalDateTime maxDate,
            Pageable pageable
    );
}
