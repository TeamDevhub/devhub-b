package teamdevhub.devhub.adapter.in.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.dto.SignupUserRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.SignupUserResponseDto;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import teamdevhub.devhub.port.in.user.UserUseCase;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @PostMapping("/signup")
    public ResponseEntity<ApiDataResponseVo<SignupUserResponseDto>> signup(@Valid @RequestBody SignupUserRequestDto signupUserRequestDto) {
        SignupCommand signupCommand = SignupCommand.fromSignupUserRequestDto(signupUserRequestDto);
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithData(
                        SuccessCodeEnum.SIGNUP_SUCCESS,
                        SignupUserResponseDto.fromUserDomain(userUseCase.signup(signupCommand))
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<Void>> updateUserProfile(@Valid @RequestBody SignupUserRequestDto signupUserRequestDto) {
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.SIGNUP_SUCCESS
                )
        );
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<Void>> withdrawUser(@Valid @RequestBody SignupUserRequestDto signupUserRequestDto) {
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.SIGNUP_SUCCESS
                )
        );
    }
}