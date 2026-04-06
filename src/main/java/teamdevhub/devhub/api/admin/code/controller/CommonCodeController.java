package teamdevhub.devhub.api.admin.code.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.admin.code.model.request.CommonCodeRequestDto;
import teamdevhub.devhub.api.admin.code.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.admin.code.port.in.facade.CommonCodeFacade;

@RestController
@RequestMapping("/admin/code")
@RequiredArgsConstructor
public class CommonCodeController {

    private final CommonCodeFacade commonCodeFacade;

    @GetMapping("/list")
    public ResponseEntity<DataListApiResponseDto<CommonCodeResponseDto>> getCommonCodeList() {
        return ResponseEntity.ok(commonCodeFacade.getCommonCodeList());
    }

    @PutMapping
    public ResponseEntity<DataApiResponseDto<?>> saveCommonCode(@RequestBody CommonCodeRequestDto code) {
        return ResponseEntity.ok(commonCodeFacade.saveCode(code.toCommonCodeCommand()));
    }
}
