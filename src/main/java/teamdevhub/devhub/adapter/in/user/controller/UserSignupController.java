package teamdevhub.devhub.adapter.in.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.adapter.in.user.UserFacade;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.response.SignupResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserSignupController {

    private final UserFacade userFacade;

    @PostMapping("/signup/email")
    public ResponseEntity<DataApiResponseDto<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.SIGNUP_SUCCESS,
                        SignupResponseDto.fromDomain(userFacade.signup(signupRequestDto.toSignupCommand()))
                )
        );
    }
}
