package teamdevhub.devhub.api.user.adapter.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teamdevhub.devhub.api.user.adapter.in.model.request.SignupRequestDto;
import teamdevhub.devhub.shared.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.user.port.in.facade.UserSignupFacade;

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
