package teamdevhub.devhub.infrastructure.user.adapter.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.infrastructure.user.adapter.in.dto.request.UpdateProfileRequestDto;
import teamdevhub.devhub.core.user.port.in.facade.UserWithdrawFacade;
import teamdevhub.devhub.infrastructure.user.adapter.in.dto.response.UserDetailResponseDto;
import teamdevhub.devhub.shared.web.model.response.DataApiResponse;
import teamdevhub.devhub.shared.web.resolver.LoginUser;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.port.in.usecase.UserProfileUseCase;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileUseCase userProfileUseCase;
    private final UserWithdrawFacade userWithdrawFacade;

    @GetMapping("/profile")
    public ResponseEntity<DataApiResponse<UserDetailResponseDto>> getProfile(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                DataApiResponse.successWithData(
                        SuccessCode.READ_SUCCESS,
                        UserDetailResponseDto.fromDomain(userProfileUseCase.getCurrentUserProfile(authenticatedUser.userGuid()))
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<DataApiResponse<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
        userProfileUseCase.updateProfile(updateProfileRequestDto.toUpdateProfileCommand(authenticatedUser.userGuid()));
        return ResponseEntity.ok(
                DataApiResponse.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }

    @DeleteMapping("/profile")
    public ResponseEntity<DataApiResponse<Void>> withdraw(@LoginUser AuthenticatedUser authenticatedUser) {
        userWithdrawFacade.withdraw(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                DataApiResponse.successWithoutData(
                        SuccessCode.USER_DELETE_SUCCESS
                )
        );
    }
}