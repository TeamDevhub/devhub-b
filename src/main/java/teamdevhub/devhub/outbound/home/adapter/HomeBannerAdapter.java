package teamdevhub.devhub.outbound.home.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.home.domain.Banner;
import teamdevhub.devhub.core.home.port.out.LoadHomeBannerPort;
import teamdevhub.devhub.outbound.home.adapter.mapper.BannerMapper;
import teamdevhub.devhub.outbound.home.persistence.JpaHomeBannerRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HomeBannerAdapter implements LoadHomeBannerPort {

    private final JpaHomeBannerRepository jpaHomeBannerRepository;

    @Override
    public List<Banner> loadMainBanners() {
        return jpaHomeBannerRepository.findExposableBanners(true, LocalDate.now())
                .stream()
                .map(BannerMapper::toDomain)
                .toList();
    }

    @Override
    public List<Banner> loadSubBanners() {
        return jpaHomeBannerRepository.findExposableBanners(false, LocalDate.now())
                .stream()
                .map(BannerMapper::toDomain)
                .toList();
    }
}
