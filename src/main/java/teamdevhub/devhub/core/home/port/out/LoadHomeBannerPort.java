package teamdevhub.devhub.core.home.port.out;

import teamdevhub.devhub.core.home.domain.Banner;

import java.util.List;

public interface LoadHomeBannerPort {

    List<Banner> loadMainBanners();

    List<Banner> loadSubBanners();
}
