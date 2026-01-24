package teamdevhub.devhub.adapter.in.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthAuthResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.in.user.dto.request.SignupOauthRequestDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SignupStatus;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.port.in.oauth.facade.OauthAuthFacade;
import teamdevhub.devhub.port.in.user.facade.UserSignupFacade;

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
        OauthAuthResponseDto oauthAuthResponseDto = oauthAuthFacade.handleOAuthCallback(provider, code);

        if (oauthAuthResponseDto.getSignupStatus().equals(SignupStatus.COMPLETED)) {
            ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(oauthAuthResponseDto.getRefreshToken());

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, oauthAuthResponseDto.toAuthorizationHeader())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(DataApiResponseDto.successWithData(
                            SuccessCode.LOGIN_SUCCESS,
                            TokenResponseDto.issueAccessToken(oauthAuthResponseDto.getAccessToken()))
                    );
        }

        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.SIGNUP_REQUIRED,
                        TokenResponseDto.issueTempToken(oauthAuthResponseDto.getTempToken())
                )
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<OauthAuthResponseDto>> signup(@RequestBody SignupOauthRequestDto signupOauthRequestDto) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.SIGNUP_SUCCESS,
                        userSignupFacade.signupWithOauth(signupOauthRequestDto.toCommand())
                )
        );
    }
}
