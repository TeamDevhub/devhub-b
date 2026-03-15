package teamdevhub.devhub.api.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.project.model.CreateProjectRequestDto;
import teamdevhub.devhub.api.project.model.SearchProjectRequestDto;
import teamdevhub.devhub.api.project.model.UpdateProjectRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.core.project.port.in.facade.model.ProjectDetailWithFormResponseDto;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
	
	private final ProjectFacade projectFacade;
	
	@PostMapping
	public ResponseEntity<DataApiResponseDto<Void>> createProject(@Valid @RequestBody CreateProjectRequestDto createProjectRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
		projectFacade.createProject(createProjectRequestDto.toCommand(authenticatedUser.userGuid()));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS));
    }

	/**
	 * 리뷰
	 * API 명세에 따라 목록조회는 list 없이 단순 GetMapping 만 명시하는 걸로 변경했습니다.
	 * @param searchProjectRequestDto
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping
    public ResponseEntity<DataListApiResponseDto<ProjectDetailResponseDto>> getProjectList(@Valid @ModelAttribute SearchProjectRequestDto searchProjectRequestDto, @RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(projectFacade.getProjectList(searchProjectRequestDto.toSearchProjectListCommand(), PageCommand.of(page, size)));
    }
	
	@GetMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<ProjectDetailResponseDto>> getProjectDetail(@PathVariable("projectGuid") String projectGuid) {
		ProjectDetailResponseDto responseDto = projectFacade.getProjectDetail(projectGuid);
		return ResponseEntity.ok(
			DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, responseDto)
		);
	
	}
	
	@GetMapping("/{projectGuid}/form")
	public ResponseEntity<DataApiResponseDto<ProjectDetailWithFormResponseDto>> getProjectDetailWithForm(@PathVariable("projectGuid") String projectGuid) {
		ProjectDetailWithFormResponseDto responseDto = projectFacade.getProjectDetailWithForm(projectGuid);
		return ResponseEntity.ok(
			DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, responseDto)
		);
	
	}
	
	@PutMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> updateProject(@PathVariable("projectGuid") String projectGuid,
			@RequestBody UpdateProjectRequestDto updateProjectRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
		projectFacade.updateProject(projectGuid, updateProjectRequestDto.toCommand());
		return ResponseEntity.ok(
			DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS)
		);
	
	}
	
	@DeleteMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> deleteProject(@PathVariable("projectGuid") String projectGuid) {
		projectFacade.deleteProject(projectGuid);
		return ResponseEntity.ok(
				DataApiResponseDto.successWithoutData(SuccessCode.DELETE_SUCCESS)
		);
	
	}
}
