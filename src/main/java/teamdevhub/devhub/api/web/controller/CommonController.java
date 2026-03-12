package teamdevhub.devhub.api.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.admin.code.model.request.CommonCodeRequestDto;
import teamdevhub.devhub.api.web.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.web.port.in.facade.CommonFacade;

import java.util.List;
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

	@PutMapping("/save")
	public ResponseEntity<DataApiResponseDto<Void>> saveCommonCode(@Valid @RequestBody CommonCodeRequestDto commonCodeRequestDto) {
		commonFacade.saveCommonCode(commonCodeRequestDto.toCommonCodeCommand());
		return ResponseEntity.ok().build();
	}

	@PutMapping("/saveAll")
	public ResponseEntity<DataApiResponseDto<Void>> saveAllCommonCode(@Valid @RequestBody List<CommonCodeRequestDto> commonCodeRequestDtoList) {
		commonFacade.saveCommonCodeList(commonCodeRequestDtoList.stream().map(CommonCodeRequestDto::toCommonCodeCommand).toList());
		return ResponseEntity.ok().build();
	}

}
