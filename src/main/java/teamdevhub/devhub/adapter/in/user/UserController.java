package teamdevhub.devhub.adapter.in.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.dto.request.user.SignupRequestDto;
import teamdevhub.devhub.adapter.in.dto.request.user.UpdateProfileRequestDto;
import teamdevhub.devhub.adapter.in.dto.response.user.SignupResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.user.UserDetailResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataResponseDto;
import teamdevhub.devhub.adapter.in.web.resolver.LoginUser;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.vo.AuthenticatedUser;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping("/signup")
    public ResponseEntity<ApiDataResponseDto<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        SignupCommand signupCommand = SignupCommand.fromSignupUserRequestDto(signupRequestDto);
        return ResponseEntity.ok(
                ApiDataResponseDto.successWithData(
                        SuccessCode.SIGNUP_SUCCESS,
                        SignupResponseDto.fromDomain(userUseCase.signup(signupCommand))
                )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiDataResponseDto<UserDetailResponseDto>> getProfile(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                ApiDataResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        UserDetailResponseDto.fromDomain(userUseCase.getCurrentUserProfile(authenticatedUser.userGuid()))
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiDataResponseDto<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.fromUpdateProfileRequestDto(updateProfileRequestDto, authenticatedUser.userGuid());
        userUseCase.updateProfile(updateProfileCommand);
        return ResponseEntity.ok(
                ApiDataResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiDataResponseDto<Void>> withdraw(@LoginUser AuthenticatedUser authenticatedUser) {
        userUseCase.withdrawUser(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                ApiDataResponseDto.successWithoutData(
                        SuccessCode.USER_DELETE_SUCCESS
                )
        );
    }
}