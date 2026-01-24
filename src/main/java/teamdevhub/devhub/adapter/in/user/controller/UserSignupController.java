package teamdevhub.devhub.adapter.in.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.port.in.user.facade.UserSignupFacade;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserSignupController {

    private final UserSignupFacade userSignupFacade;

    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<Void>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userSignupFacade.signup(signupRequestDto.toSignupCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.SIGNUP_SUCCESS
                )
        );
    }
}
