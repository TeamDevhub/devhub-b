package teamdevhub.devhub.outbound.admin.banner.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.admin.banner.domain.Banner;
import teamdevhub.devhub.core.admin.banner.port.in.command.SearchBannerRequestCommand;
import teamdevhub.devhub.core.admin.banner.port.out.BannerRepository;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.outbound.admin.banner.adapter.entity.BannerEntity;
import teamdevhub.devhub.outbound.admin.banner.adapter.mapper.BannerMapper;
import teamdevhub.devhub.outbound.admin.banner.persistence.JpaBannerRepository;

import static teamdevhub.devhub.shared.enums.DateConstants.MAX_LOCAL_DATE_TIME;

@Component
@RequiredArgsConstructor
public class BannerAdapter implements BannerRepository {

    private final JpaBannerRepository jpaBannerRepository;

    @Override
    public PageResult<Banner> getBannerList(SearchBannerRequestCommand searchBannerRequestCommand, PageCommand pageCommand) {
        Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size());
        Page<BannerEntity> resultList = jpaBannerRepository.findByComplexCondition(
                searchBannerRequestCommand.keyword()
                , searchBannerRequestCommand.isUsed() ? "Y" : "N"
                , searchBannerRequestCommand.isMainBanner() ? "Y" : "N"
                , searchBannerRequestCommand.alwaysPublication()
                , searchBannerRequestCommand.publicationStartDate()
                , searchBannerRequestCommand.publicationEndDate()
                , MAX_LOCAL_DATE_TIME
                , pageable
        );
        return PageResult.of(
                resultList.getContent().stream().map(BannerMapper::toBanner).toList(),
                resultList.getNumber(),
                resultList.getSize(),
                resultList.getTotalElements()
        );
    }
}
