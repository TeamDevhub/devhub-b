package teamdevhub.devhub.core.admin.banner.port.out;

import teamdevhub.devhub.core.admin.banner.domain.Banner;
import teamdevhub.devhub.core.admin.banner.port.in.command.SearchBannerRequestCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;

public interface BannerRepository {
    PageResult<Banner> getBannerList(SearchBannerRequestCommand searchBannerRequestCommand, PageCommand pageCommand);
    void saveBanner(Banner command);
    void deleteBanner(String bannerGuid);
}
