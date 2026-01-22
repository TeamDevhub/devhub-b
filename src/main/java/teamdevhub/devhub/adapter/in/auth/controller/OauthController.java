package teamdevhub.devhub.adapter.in.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.adapter.in.auth.dto.request.OauthLoginRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupOauthRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.port.in.auth.AuthFacade;
import teamdevhub.devhub.port.in.user.UserSignupFacade;

@RestController
@RequestMapping("/auth/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final UserSignupFacade userSignupFacade;
    private final AuthFacade authFacade;

    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<OauthAuthResponseDto>> signup(@RequestBody SignupOauthRequestDto signupOauthRequestDto) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.SIGNUP_SUCCESS,
                        userSignupFacade.signupWithOauth(signupOauthRequestDto.toCommand())
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> login(@RequestBody OauthLoginRequestDto oauthLoginRequestDto) {
        OauthAuthResponseDto oauthAuthResponseDto = authFacade.loginWithOauth(oauthLoginRequestDto.toCommand());
        ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(oauthAuthResponseDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, oauthAuthResponseDto.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issue(oauthAuthResponseDto.getAccessToken()))
                );
    }
}
