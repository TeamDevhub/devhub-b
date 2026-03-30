package teamdevhub.devhub.outbound.admin.banner.adapter.mapper;

import teamdevhub.devhub.core.admin.banner.domain.Banner;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.outbound.admin.banner.adapter.entity.BannerEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BannerMapper {

    private static AuditInfo toAuditInfo(BannerEntity entity) {
        return AuditInfo.of(
                entity.getRegistrantGuid(),
                entity.getRegisteredDate(),
                entity.getModifierGuid(),
                entity.getModifiedDate()
        );
    }

    public static Banner toBanner(BannerEntity bannerEntity) {
        return Banner.builder()
                .bannerGuid(bannerEntity.getBannerGuid())
                .publicationStartDate(bannerEntity.getPublicationStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .publicationEndDate(bannerEntity.getPublicationEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .imageGuid(bannerEntity.getImageGuid())
                .title(bannerEntity.getTitle())
                .description(bannerEntity.getDescription())
                .isUsed(bannerEntity.isUsed())
                .isMainBanner(bannerEntity.isMainBanner())
                .link(bannerEntity.getLink())
                .auditInfo(toAuditInfo(bannerEntity))
                .build();
    }

    public static BannerEntity toEntity(Banner banner) {
        return BannerEntity.builder()
                .bannerGuid(banner.getBannerGuid())
                .link(banner.getLink())
                .title(banner.getTitle())
                .publicationStartDate(LocalDate.parse(banner.getPublicationStartDate()).atStartOfDay())
                .publicationEndDate(LocalDate.parse(banner.getPublicationEndDate()).atStartOfDay())
                .description(banner.getDescription())
                .isMainBanner(banner.isMainBanner())
                .used(banner.isUsed())
                .imageGuid(banner.getImageGuid())
                .build();
    }
}
