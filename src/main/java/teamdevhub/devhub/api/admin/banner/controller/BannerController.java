package teamdevhub.devhub.api.admin.banner.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.admin.banner.model.request.SearchBannerRequestDto;
import teamdevhub.devhub.api.admin.banner.model.response.BannerResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.admin.banner.port.in.facade.BannerFacade;
import teamdevhub.devhub.core.common.page.PageCommand;

@RestController
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class BannerController {

    private  final BannerFacade bannerFacade;

    @GetMapping("/list")
    public ResponseEntity<DataListApiResponseDto<BannerResponseDto>> getBannerList(@Valid @ModelAttribute SearchBannerRequestDto searchBannerRequestDto, @RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(bannerFacade.getBannerList(searchBannerRequestDto.toCommand(), PageCommand.of(page, size)));
    }
}
