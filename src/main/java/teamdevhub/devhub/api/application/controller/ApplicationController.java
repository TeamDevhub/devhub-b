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

	@GetMapping("/{projectGuid}/applications/{applicationGuid}")
	public ResponseEntity<DataApiResponseDto<ProjectApplicationDetailWrapperResponseDto>> getApplicationDetail(
		@PathVariable("projectGuid") String projectGuid,
		@PathVariable("applicationGuid") String applicationGuid
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.getApplicationDetail(applicationGuid)
		);
	}
}
