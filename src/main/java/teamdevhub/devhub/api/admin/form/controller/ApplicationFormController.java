package teamdevhub.devhub.api.admin.form.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.api.admin.form.model.CreateApplicationFormRequestDto;
import teamdevhub.devhub.api.admin.form.model.SearchAdminFormRequestDto;
import teamdevhub.devhub.api.admin.form.model.UpdateApplicationFormRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.admin.form.port.in.facade.ApplicationFormFacade;
import teamdevhub.devhub.core.admin.form.port.in.facade.ApplicationFormFacade.ApplicationFormListItemDto;
import teamdevhub.devhub.core.common.page.PageCommand;

@Tag(name = "Admin - Form", description = "관리자 지원 폼 관리 API")
@RestController
@RequestMapping("/admin/form")
@RequiredArgsConstructor
public class ApplicationFormController {

	private final ApplicationFormFacade applicationFormFacade;

	@Operation(summary = "지원 폼 목록 조회", description = "제목으로 검색하여 지원 폼 목록을 페이징 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/list")
	public ResponseEntity<DataListApiResponseDto<ApplicationFormListItemDto>> getApplicationForms(
			@ModelAttribute SearchAdminFormRequestDto searchDto,
			@Parameter(description = "페이지 번호", example = "0") @RequestParam int page,
			@Parameter(description = "페이지 크기", example = "10") @RequestParam int size) {
		return ResponseEntity.ok(
				applicationFormFacade.getApplicationForms(searchDto.toCommand(), PageCommand.of(page, size))
		);
	}

	@Operation(summary = "지원 폼 등록", description = "새 지원 폼을 등록합니다.")
	@ApiResponse(responseCode = "200", description = "등록 성공")
	@PostMapping
	public ResponseEntity<DataApiResponseDto<Void>> createApplicationForm(
			@Valid @RequestBody CreateApplicationFormRequestDto requestDto) {
		return ResponseEntity.ok(
				applicationFormFacade.createApplicationForm(requestDto.toCommand())
		);
	}

	@Operation(summary = "지원 폼 수정", description = "지원 폼 GUID로 폼 정보를 수정합니다.")
	@ApiResponse(responseCode = "200", description = "수정 성공")
	@PutMapping("/{applicationFormGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> updateApplicationForm(
			@Parameter(description = "지원 폼 GUID", required = true) @PathVariable String applicationFormGuid,
			@Valid @RequestBody UpdateApplicationFormRequestDto requestDto) {
		return ResponseEntity.ok(
				applicationFormFacade.updateApplicationForm(applicationFormGuid, requestDto.toCommand())
		);
	}
}
