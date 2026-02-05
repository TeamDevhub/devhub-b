package teamdevhub.devhub.api.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.api.web.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.web.port.in.facade.CommonFacade;

import java.util.Map;

@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {
	
	private final CommonFacade commonFacade;
	
	@GetMapping("/code")
    public ResponseEntity<DataApiResponseDto<Map<String, CommonCodeResponseDto>>> getCommonCodeList() {
        return ResponseEntity.ok(commonFacade.getCommonCodeList());
	}

}
