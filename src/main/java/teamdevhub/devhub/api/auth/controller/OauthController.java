package teamdevhub.devhub.api.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import teamdevhub.devhub.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.api.user.model.request.SignupOauthRequestDto;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthResult;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.shared.enums.SignupStatus;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.auth.port.in.facade.OauthAuthFacade;
import teamdevhub.devhub.core.user.port.in.facade.UserSignupFacade;

import java.io.IOException;

@RestController
@RequestMapping("/auth/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final UserSignupFacade userSignupFacade;
    private final OauthAuthFacade oauthAuthFacade;

    @GetMapping("/{provider}")
    public void redirectToProvider(@PathVariable String provider, HttpServletResponse httpServletResponse) throws IOException {
        String authorizationUrl = oauthAuthFacade.createOAuthAuthorizationUrl(provider);
        httpServletResponse.sendRedirect(authorizationUrl);
    }

    @GetMapping("/{provider}/callback")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> handleOauthCallback(@PathVariable String provider, @RequestParam String code) {
        OauthAuthResult oauthAuthResult = oauthAuthFacade.handleOAuthCallback(provider, code);

        if (oauthAuthResult.signupStatus().equals(SignupStatus.COMPLETED)) {
            ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(oauthAuthResult.refreshToken());

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, oauthAuthResult.toAuthorizationHeader())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(DataApiResponseDto.successWithData(
                            SuccessCode.LOGIN_SUCCESS,
                            TokenResponseDto.issueAccessToken(oauthAuthResult.accessToken()))
                    );
        }

        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.SIGNUP_REQUIRED,
                        TokenResponseDto.issueTempToken(oauthAuthResult.tempToken())
                )
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> signup(@RequestBody SignupOauthRequestDto signupOauthRequestDto) {
        OauthAuthResult oauthAuthResult = userSignupFacade.signupWithOauth(signupOauthRequestDto.toCommand());
        ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(oauthAuthResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, oauthAuthResult.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issueAccessToken(oauthAuthResult.accessToken()))
                );
    }
}
