package teamdevhub.devhub.api.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teamdevhub.devhub.api.auth.controller.CookieFactory;
import teamdevhub.devhub.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.api.user.model.SignupRequestDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.auth.application.service.AuthResult;
import teamdevhub.devhub.core.auth.port.in.facade.AuthFacade;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.user.port.in.facade.UserSignupFacade;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserSignupController {

    private final UserSignupFacade userSignupFacade;
    private final AuthFacade authFacade;

    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        userSignupFacade.signup(signupRequestDto.toSignupCommand());
        AuthResult authResult = authFacade.login(signupRequestDto.toLoginCommand());
        ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(authResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authResult.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issueAccessToken(authResult.accessToken()))
                );
    }
}
