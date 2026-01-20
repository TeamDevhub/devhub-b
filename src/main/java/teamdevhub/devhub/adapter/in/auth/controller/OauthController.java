package teamdevhub.devhub.adapter.in.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.adapter.in.auth.AuthFacade;
import teamdevhub.devhub.adapter.in.auth.CookieFactory;
import teamdevhub.devhub.adapter.in.auth.dto.request.OauthLoginRequestDto;
import teamdevhub.devhub.adapter.in.user.dto.request.OauthSignupRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;

@RestController
@RequestMapping("/auth/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final AuthFacade authFacade;

    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<LoginResponseDto>> signup(@RequestBody OauthSignupRequestDto oauthSignupRequestDto) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        authFacade.signupWithOauth(oauthSignupRequestDto.toCommand())
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> login(@RequestBody OauthLoginRequestDto oauthLoginRequestDto) {
        LoginResponseDto loginResponseDto = authFacade.loginWithOauth(oauthLoginRequestDto.toCommand());
        ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(loginResponseDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, loginResponseDto.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issue(loginResponseDto.getAccessToken()))
                );
    }
}
