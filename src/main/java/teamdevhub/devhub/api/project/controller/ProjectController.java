package teamdevhub.devhub.api.project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.project.model.request.ProjectListSearchRequestDto;
import teamdevhub.devhub.api.project.model.response.ProjectDetailResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
	
	private final ProjectFacade projectFacade;
	
	@GetMapping("/list")
    public ResponseEntity<DataListApiResponseDto<ProjectDetailResponseDto>> getProjectList(@Valid @ModelAttribute ProjectListSearchRequestDto projectListSearchRequestDto, @RequestParam("page") int page, @RequestParam("size") int size) {
		
		PageResult<ProjectDetail> pagedProjectList = projectFacade.getProjectList(projectListSearchRequestDto.toSearchProjectListCommaond(), PageCommand.of(page, size)); 
        List<ProjectDetailResponseDto> projectDetailResponseDtoList = pagedProjectList.content().stream()
                .map(ProjectDetailResponseDto::fromDomain)
                .toList();
        
        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        projectDetailResponseDtoList,
                        PageResponseDto.from(pagedProjectList))
                );
    }
	
	
}
