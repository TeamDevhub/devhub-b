package teamdevhub.devhub.api.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.api.project.model.ReviewUserRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.port.in.facade.UserReviewFacade;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectMemberReviewController {

    private final UserReviewFacade userReviewFacade;

    @PostMapping("/{projectGuid}/members/{userGuid}")
    public ResponseEntity<DataApiResponseDto<Void>> reviewMember(
            @PathVariable String projectGuid,
            @PathVariable String userGuid,
            @Valid @RequestBody ReviewUserRequestDto reviewUserRequestDto,
            @LoginUser AuthenticatedUser authenticatedUser) {

        userReviewFacade.reviewMember(reviewUserRequestDto.toReviewUserCommand(projectGuid, userGuid, reviewUserRequestDto, authenticatedUser));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.REVIEW_SUCCESS));
    }
}
