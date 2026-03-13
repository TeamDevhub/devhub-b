package teamdevhub.devhub.core.admin.banner.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.admin.banner.domain.Banner;
import teamdevhub.devhub.core.admin.banner.port.in.command.SearchBannerRequestCommand;
import teamdevhub.devhub.core.admin.banner.port.in.usecase.BannerUseCase;
import teamdevhub.devhub.core.admin.banner.port.out.BannerRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

@Service
@Transactional
@RequiredArgsConstructor
public class BannerService implements BannerUseCase {

    private final BannerRepository bannerRepository;

    @Override
    public PageResult<Banner> getBannerList(SearchBannerRequestCommand searchBannerRequestCommand, PageCommand pageCommand) {
        return bannerRepository.getBannerList(searchBannerRequestCommand, pageCommand);
    }

}
