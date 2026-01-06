package teamdevhub.devhub.adapter.in.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.adapter.in.user.dto.response.UserProfileResponseDto;
import teamdevhub.devhub.adapter.out.auth.userDetail.AuthenticatedUserDetails;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import teamdevhub.devhub.port.in.user.UserUseCase;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping("/signup")
    public ResponseEntity<ApiDataResponseVo<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        SignupCommand signupCommand = SignupCommand.fromSignupUserRequestDto(signupRequestDto);
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithData(
                        SuccessCodeEnum.SIGNUP_SUCCESS,
                        SignupResponseDto.fromDomain(userUseCase.signup(signupCommand))
                )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<UserProfileResponseDto>> getProfile(@AuthenticationPrincipal AuthenticatedUserDetails userDetails) {
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithData(
                        SuccessCodeEnum.READ_SUCCESS,
                        UserProfileResponseDto.fromDomain(userUseCase.getCurrentUserProfile(userDetails.getUserGuid()))
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto, @AuthenticationPrincipal AuthenticatedUserDetails userDetails) {
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.fromUpdateProfileRequestDto(updateProfileRequestDto, userDetails.getUserGuid());
        userUseCase.updateProfile(updateProfileCommand);
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.UPDATE_SUCCESS
                )
        );
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<Void>> withdraw(@AuthenticationPrincipal AuthenticatedUserDetails userDetails) {
        userUseCase.withdrawCurrentUser(userDetails.getUserGuid());
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.USER_DELETE_SUCCESS
                )
        );
    }
}