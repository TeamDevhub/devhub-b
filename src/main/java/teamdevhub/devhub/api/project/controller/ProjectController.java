package teamdevhub.devhub.api.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.project.model.request.CreateProjectRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.project.port.in.facade.ProjectCreateFacade;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
//@RequestMapping("/user")
@RequiredArgsConstructor
public class ProjectController {
	
	private final ProjectCreateFacade projectCreateFacade;
	
	@PostMapping("/projects")
	public ResponseEntity<DataApiResponseDto<Void>> createProject(@Valid @RequestPart("request") CreateProjectRequestDto createProjectRequestDto, @LoginUser AuthenticatedUser authenticatedUser,
			@RequestPart(value = "attachment", required = false) MultipartFile attachment, @RequestPart(value = "image", required = false) MultipartFile image) {
		projectCreateFacade.createProject(createProjectRequestDto.toCommand(authenticatedUser.userGuid(), authenticatedUser.username()), attachment, image);
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }
}
