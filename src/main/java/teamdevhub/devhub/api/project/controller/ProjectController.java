package teamdevhub.devhub.api.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.project.model.request.ProjectListSearchRequestDto;
import teamdevhub.devhub.api.project.model.response.ProjectDetailResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.project.port.in.facade.ProjectFacade;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
	
	private final ProjectFacade projectFacade;
	
	@GetMapping
    public ResponseEntity<DataListApiResponseDto<ProjectDetailResponseDto>> getProjectList(@Valid @ModelAttribute ProjectListSearchRequestDto projectListSearchRequestDto, @RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseEntity.ok(projectFacade.getProjectList(projectListSearchRequestDto.toSearchProjectListCommaond(), PageCommand.of(page, size)));
    }
	
	
}
