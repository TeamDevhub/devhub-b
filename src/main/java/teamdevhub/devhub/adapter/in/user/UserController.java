package teamdevhub.devhub.adapter.in.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.dto.SignupRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.SignupResponseDto;
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
                        SignupResponseDto.fromUserDomain(userUseCase.signup(signupCommand))
                )
        );
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<Void>> updateUserProfile(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.SIGNUP_SUCCESS
                )
        );
    }

    @DeleteMapping("/profile")
    public ResponseEntity<ApiDataResponseVo<Void>> withdrawUser(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.SIGNUP_SUCCESS
                )
        );
    }
}