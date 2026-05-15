package teamdevhub.devhub.outbound.home.persistence;

import teamdevhub.devhub.outbound.admin.banner.adapter.entity.BannerEntity;

import java.time.LocalDate;
import java.util.List;

public interface HomeBannerQueryDao {

    List<BannerEntity> findExposableBanners(boolean mainBanner, LocalDate today);
}
