package teamdevhub.devhub.outbound.home.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import teamdevhub.devhub.outbound.admin.banner.adapter.entity.BannerEntity;

import java.time.LocalDate;
import java.util.List;

import static teamdevhub.devhub.outbound.admin.banner.adapter.entity.QBannerEntity.bannerEntity;

@Repository
@RequiredArgsConstructor
public class HomeBannerQueryDaoImpl implements HomeBannerQueryDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BannerEntity> findExposableBanners(boolean mainBanner, LocalDate today) {
        return queryFactory
                .selectFrom(bannerEntity)
                .where(
                        bannerEntity.mainBanner.eq(mainBanner),
                        bannerEntity.used.isTrue(),
                        bannerEntity.startDate.loe(today),
                        bannerEntity.endDate.goe(today)
                )
                .orderBy(bannerEntity.sortOrder.asc())
                .fetch();
    }
}
