package teamdevhub.devhub.api.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.application.model.request.SearchApplicationFormRequestDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.application.port.in.facade.ApplicationFormFacade;
import teamdevhub.devhub.core.application.port.in.facade.model.ApplicationFormResponseDto;

@RestController
@RequestMapping("/applicationForms")
@RequiredArgsConstructor
public class ApplicationFormController {
	
	private final ApplicationFormFacade applicationFormFacade;
	
	@GetMapping
	public ResponseEntity<DataListApiResponseDto<ApplicationFormResponseDto>> getApplicationForms(@Valid @ModelAttribute SearchApplicationFormRequestDto searchApplicationFormRequestDto){
		return ResponseEntity.ok(applicationFormFacade.getApplicationFormsWithoutItem(searchApplicationFormRequestDto.toCommand()));
	}

}
