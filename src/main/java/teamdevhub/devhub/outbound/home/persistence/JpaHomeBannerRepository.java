package teamdevhub.devhub.outbound.home.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import teamdevhub.devhub.outbound.admin.banner.adapter.entity.BannerEntity;

import java.time.LocalDate;
import java.util.List;

public interface JpaHomeBannerRepository extends JpaRepository<BannerEntity, String> {

    @Query("""
            SELECT b FROM BannerEntity b
            WHERE b.mainBanner = :mainBanner
            AND b.used = true
            AND b.startDate <= :today
            AND b.endDate >= :today
            ORDER BY b.sortOrder ASC
            """)
    List<BannerEntity> findExposableBanners(
            @Param("mainBanner") boolean mainBanner,
            @Param("today") LocalDate today
    );
}
