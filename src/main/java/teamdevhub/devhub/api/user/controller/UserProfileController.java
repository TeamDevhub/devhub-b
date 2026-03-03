package teamdevhub.devhub.api.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.api.user.model.UpdateProfileImageRequestDto;
import teamdevhub.devhub.api.user.model.UpdateProfileRequestDto;
import teamdevhub.devhub.core.user.port.in.facade.model.UserBasicResponseDto;
import teamdevhub.devhub.core.user.port.in.facade.model.UserDetailResponseDto;
import teamdevhub.devhub.core.user.port.in.facade.UserProfileFacade;
import teamdevhub.devhub.core.user.port.in.facade.UserWithdrawFacade;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.SuccessCode;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileFacade userProfileFacade;
    private final UserWithdrawFacade userWithdrawFacade;

    @GetMapping()
    public ResponseEntity<DataApiResponseDto<UserBasicResponseDto>> getUserInfo(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        userProfileFacade.getUserInfo(authenticatedUser.userGuid())
                )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<DataApiResponseDto<UserDetailResponseDto>> getProfile(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        userProfileFacade.getCurrentUserProfile(authenticatedUser.userGuid())
                )
        );
    }

    @PostMapping("/profile/image")
    public ResponseEntity<DataApiResponseDto<Void>> updateProfileImage(@Valid @RequestBody UpdateProfileImageRequestDto updateProfileImageRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
        userProfileFacade.updateProfileImage(updateProfileImageRequestDto.toUpdateProfileImageCommand(authenticatedUser.userGuid()));
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<DataApiResponseDto<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
        userProfileFacade.updateProfile(updateProfileRequestDto.toUpdateProfileCommand(authenticatedUser.userGuid()));
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }

    @DeleteMapping("/profile")
    public ResponseEntity<DataApiResponseDto<Void>> withdraw(@LoginUser AuthenticatedUser authenticatedUser) {
        userWithdrawFacade.withdraw(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.USER_DELETE_SUCCESS
                )
        );
    }
}