package teamdevhub.devhub.outbound.home.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.outbound.admin.banner.adapter.entity.BannerEntity;

public interface JpaHomeBannerRepository extends JpaRepository<BannerEntity, String> {
}
