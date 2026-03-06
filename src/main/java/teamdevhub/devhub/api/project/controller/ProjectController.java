package teamdevhub.devhub.api.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.project.model.CreateProjectRequestDto;
import teamdevhub.devhub.api.project.model.SearchProjectRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
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
	
	@PutMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<ProjectDetailResponseDto>> getProjectDetail(@PathVariable("projectGuid") String projectGuid) {
		ProjectDetailResponseDto responseDto = projectFacade.getProjectDetail(projectGuid);
		return ResponseEntity.ok(
			DataApiResponseDto.successWithData(
				SuccessCode.READ_SUCCESS,
				responseDto)
		);
	
	}
	
	@GetMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> updateProject(@PathVariable("projectGuid") String projectGuid) {
		projectFacade.updateProject(projectGuid);
		return ResponseEntity.ok(
			DataApiResponseDto.successWithoutData(
				SuccessCode.UPDATE_SUCCESS)
		);
	
	}
	
	@DeleteMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> deleteProject(@PathVariable("projectGuid") String projectGuid) {
		projectFacade.deleteProjectDetail(projectGuid);
		return ResponseEntity.ok(
			DataApiResponseDto.successWithoutData(
				SuccessCode.DELETE_SUCCESS)
		);
	
	}
	
	
}
