package teamdevhub.devhub.api.admin.code.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.api.admin.code.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.admin.code.port.in.facade.CommonCodeFacade;

@RestController
@RequestMapping("/admin/code")
@RequiredArgsConstructor
public class CommonCodeController {

    private  final CommonCodeFacade commonCodeFacade;

    @GetMapping("/list")
    public ResponseEntity<DataListApiResponseDto<CommonCodeResponseDto>> getCommonCodeList() {
        return ResponseEntity.ok(commonCodeFacade.getCommonCodeList());
    }



}
