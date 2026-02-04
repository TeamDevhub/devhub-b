package teamdevhub.devhub.api.project.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.project.model.request.CreateProjectRequestDto;
import teamdevhub.devhub.api.project.model.request.ProjectListSearchRequestDto;
import teamdevhub.devhub.api.project.model.response.ProjectDetailResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.core.project.domain.ProjectDetail;
import teamdevhub.devhub.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
	
	private final ProjectFacade projectFacade;
	
	@PostMapping
	public ResponseEntity<DataApiResponseDto<Void>> createProject(@Valid @RequestPart("request") CreateProjectRequestDto createProjectRequestDto, @LoginUser AuthenticatedUser authenticatedUser,
			@RequestPart(value = "attachment", required = false) MultipartFile attachment, @RequestPart(value = "image", required = false) MultipartFile image) {
		projectFacade.createProject(createProjectRequestDto.toCommand(authenticatedUser.userGuid(), authenticatedUser.username()), attachment, image);
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }
	
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
