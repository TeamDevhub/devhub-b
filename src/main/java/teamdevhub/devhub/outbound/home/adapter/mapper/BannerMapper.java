package teamdevhub.devhub.outbound.home.adapter.mapper;

import teamdevhub.devhub.core.home.domain.Banner;
import teamdevhub.devhub.outbound.admin.banner.adapter.entity.BannerEntity;

public class BannerMapper {

    private BannerMapper() {}

    public static Banner toDomain(BannerEntity entity) {
        return Banner.of(
                entity.getBannerGuid(),
                entity.getTitle(),
                entity.getImageFileGuid(),
                entity.getLinkUrl(),
                entity.isMainBanner(),
                entity.isUsed(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getSortOrder()
        );
    }
}
