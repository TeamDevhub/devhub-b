package teamdevhub.devhub.api.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.project.model.request.ProjectListSearchRequestDto;
import teamdevhub.devhub.api.project.model.response.ProjectDetailResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
	
	private final ProjectFacade projectFacade;
	
	@PostMapping
	public ResponseEntity<DataApiResponseDto<Void>> createProject(@Valid @RequestBody CreateProjectRequestDto createProjectRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
		projectFacade.createProject(createProjectRequestDto.toCommand(authenticatedUser.userGuid(), authenticatedUser.username()));
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.CREATE_SUCCESS
                )
        );
    }

	@GetMapping("/list")
    public ResponseEntity<DataListApiResponseDto<ProjectDetailResponseDto>> getProjectList(@Valid @ModelAttribute SearchProjectRequestDto searchProjectRequestDto, @RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(projectFacade.getProjectList(searchProjectRequestDto.toSearchProjectListCommand(), PageCommand.of(page, size)));
    }
	
	@GetMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<ProjectDetailResponseDto>> getProjectDetail( @PathVariable("projectGuid") String projectGuid) {
		ProjectDetail projectDetail = projectFacade.getProjectDetail(projectGuid);
		ProjectDetailResponseDto responseDto = ProjectDetailResponseDto.fromDomain(projectDetail);
		
		return ResponseEntity.ok(
			DataApiResponseDto.successWithData(
				SuccessCode.READ_SUCCESS,
				responseDto)
		);
	
	}
}
