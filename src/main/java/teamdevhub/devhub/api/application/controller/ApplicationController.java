package teamdevhub.devhub.api.application.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.application.model.request.CreateApplicationRequestDto;
import teamdevhub.devhub.api.application.model.response.ProjectApplicationDetailWrapperResponseDto;
import teamdevhub.devhub.api.application.model.response.ProjectApplicationListResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.application.port.in.facade.ProjectApplicationFacade;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ApplicationController {

	private final ProjectApplicationFacade projectApplicationFacade;

	/**
	 * 리뷰
	 * @PathVaribale 로 작성하신 projectGuid를 따로 사용하진 않는데, 혹시 사용하신 이유가 있을까요?
	 * HTTP 메서드 PATCH 는 사용하지 않고 PutMapping 으로 사용하도록 컨벤션에 적어놓았습니다.
	 */
	@PutMapping("/applications/{applicationGuid}/approve")
	public ResponseEntity<DataApiResponseDto<Void>> approveApplication(
		@PathVariable("applicationGuid") String applicationGuid,
		@RequestParam("approved") boolean approved,
		@LoginUser AuthenticatedUser authenticatedUser
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.approveApplication(applicationGuid, authenticatedUser.userGuid(), approved)
		);
	}

	@PostMapping("/{projectGuid}/applications")
	public ResponseEntity<DataApiResponseDto<Void>> createApplication(
		@PathVariable("projectGuid") String projectGuid,
		@Valid @RequestBody CreateApplicationRequestDto requestDto,
		@LoginUser AuthenticatedUser authenticatedUser
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.createApplication(projectGuid, authenticatedUser.userGuid(), requestDto)
		);
	}

	@GetMapping("/{projectGuid}/applications")
	public ResponseEntity<DataApiResponseDto<ProjectApplicationListResponseDto>> getApplicationsByProjectGuid(
		@PathVariable("projectGuid") String projectGuid,
		@RequestParam("page") int page,
		@RequestParam("size") int size
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.getApplicationsByProjectGuid(projectGuid, PageCommand.of(page, size))
		);
	}

	/**
	 * 리뷰
	 * @PathVaribale 로 작성하신 projectGuid를 따로 사용하진 않는데, 혹시 사용하신 이유가 있을까요?
	 */
	@GetMapping("/applications/{applicationGuid}")
	public ResponseEntity<DataApiResponseDto<ProjectApplicationDetailWrapperResponseDto>> getApplicationDetail(
		@PathVariable("applicationGuid") String applicationGuid
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.getApplicationDetail(applicationGuid)
		);
	}
}
