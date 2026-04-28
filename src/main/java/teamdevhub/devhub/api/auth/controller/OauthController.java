package teamdevhub.devhub.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import teamdevhub.devhub.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.api.user.model.SignupOauthRequestDto;
import teamdevhub.devhub.core.auth.application.service.oauth.OauthAuthResult;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.auth.port.in.facade.OauthAuthFacade;
import teamdevhub.devhub.core.user.port.in.facade.UserSignupFacade;

import java.io.IOException;

@Tag(name = "OAuth", description = "소셜 로그인(Google·GitHub·Kakao·Naver) API")
@RestController
@RequestMapping("/auth/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final UserSignupFacade userSignupFacade;
    private final OauthAuthFacade oauthAuthFacade;

    @Operation(summary = "OAuth 인가 URL 리다이렉트", description = "지정된 OAuth 제공자의 로그인 페이지로 리다이렉트합니다. (google, github, kakao, naver)")
    @ApiResponse(responseCode = "302", description = "OAuth 제공자 인가 페이지로 리다이렉트")
    @GetMapping("/{provider}")
    public void redirectToProvider(
            @Parameter(description = "OAuth 제공자 (google, github, kakao, naver)", example = "google", required = true)
            @PathVariable String provider,
            HttpServletResponse httpServletResponse) throws IOException {
        String authorizationUrl = oauthAuthFacade.createOAuthAuthorizationUrl(provider);
        httpServletResponse.sendRedirect(authorizationUrl);
    }

    @Operation(summary = "OAuth 콜백 처리", description = "OAuth 제공자로부터 인가 코드를 받아 로그인 또는 회원가입 흐름을 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "로그인 완료 시 메인 페이지로, 미가입 시 회원가입 페이지로 리다이렉트")
    })
    @GetMapping("/{provider}/callback")
    public void handleOauthCallback(
            @Parameter(description = "OAuth 제공자", example = "google", required = true) @PathVariable String provider,
            @Parameter(description = "OAuth 제공자로부터 받은 인가 코드", required = true) @RequestParam String code,
            HttpServletResponse response) throws IOException {
        OauthAuthResult oauthAuthResult = oauthAuthFacade.handleOAuthCallback(provider, code);

        if (oauthAuthResult.signupStatus().equals(SignupStatus.COMPLETED)) {
            ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(oauthAuthResult.refreshToken());
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            response.sendRedirect("http://localhost:5173/");
        } else {
            String redirectUrl = "http://localhost:5173/auth/signup" + "?token=" + oauthAuthResult.tempToken();
            response.sendRedirect(redirectUrl);
        }
    }

    @Operation(summary = "OAuth 회원가입 완료", description = "임시 토큰과 추가 프로필 정보를 이용하여 OAuth 회원가입을 완료합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공, Access Token 반환"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 임시 토큰 또는 요청 데이터")
    })
    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> signup(@RequestBody SignupOauthRequestDto signupOauthRequestDto) {
        OauthAuthResult oauthAuthResult = userSignupFacade.signupWithOauth(signupOauthRequestDto.toSignupOauthUserCommand());
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
