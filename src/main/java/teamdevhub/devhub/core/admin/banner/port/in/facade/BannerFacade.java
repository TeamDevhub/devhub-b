package teamdevhub.devhub.core.admin.banner.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.api.admin.banner.model.response.BannerResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.admin.banner.domain.Banner;
import teamdevhub.devhub.core.admin.banner.port.in.command.BannerCommand;
import teamdevhub.devhub.core.admin.banner.port.in.command.SearchBannerRequestCommand;
import teamdevhub.devhub.core.admin.banner.port.in.usecase.BannerUseCase;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.shared.enums.SuccessCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerFacade {

    private final BannerUseCase bannerUseCase;

    public DataListApiResponseDto<BannerResponseDto> getBannerList(SearchBannerRequestCommand searchBannerRequestCommand, PageCommand pageCommand) {
        PageResult<Banner> pageDataList = bannerUseCase.getBannerList(searchBannerRequestCommand, pageCommand);
        List<BannerResponseDto> returnData = pageDataList.content().stream()
                .map(BannerResponseDto::fromDomain)
                .toList();

        return DataListApiResponseDto.successWithDataList(
                SuccessCode.READ_SUCCESS,
                returnData,
                PageResponseDto.from(pageDataList)
        );
    }

    public DataApiResponseDto<?> saveBanner(BannerCommand command) {
        bannerUseCase.saveBanner(command);
        return DataApiResponseDto.successWithData(
                SuccessCode.READ_SUCCESS,
                null
        );
    }

    public DataApiResponseDto<?> deleteBanner(String bannerGuid) {
        bannerUseCase.deleteBanner(bannerGuid);
        return DataApiResponseDto.successWithData(
                SuccessCode.READ_SUCCESS,
                null
        );
    }
}
