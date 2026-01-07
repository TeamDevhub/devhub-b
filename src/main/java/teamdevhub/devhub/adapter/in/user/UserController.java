package teamdevhub.devhub.adapter.in.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.common.annotation.LoginUser;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.request.UpdateProfileRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.adapter.in.user.dto.response.UserProfileResponseDto;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;
import teamdevhub.devhub.port.in.user.UserUseCase;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
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

    @PostMapping("/signup/{oauth}")
    public ResponseEntity<ApiDataResponseVo<Void>> signupWithOauth(@PathVariable String oauth) {
        //userUseCase.oauthSignup(oauth)
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.SIGNUP_SUCCESS
                        //SignupResponseDto.fromDomain(userUseCase.signupWithOauth(oauth))
                )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<UserProfileResponseDto>> getProfile(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithData(
                        SuccessCodeEnum.READ_SUCCESS,
                        UserProfileResponseDto.fromDomain(userUseCase.getCurrentUserProfile(authenticatedUser.userGuid()))
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.fromUpdateProfileRequestDto(updateProfileRequestDto, authenticatedUser.userGuid());
        userUseCase.updateProfile(updateProfileCommand);
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.UPDATE_SUCCESS
                )
        );
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<Void>> withdraw(@LoginUser AuthenticatedUser authenticatedUser) {
        userUseCase.withdrawCurrentUser(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.USER_DELETE_SUCCESS
                )
        );
    }
}