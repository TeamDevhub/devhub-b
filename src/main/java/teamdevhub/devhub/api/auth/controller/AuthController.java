package teamdevhub.devhub.api.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import teamdevhub.devhub.api.auth.model.request.LoginRequestDto;
import teamdevhub.devhub.core.auth.application.service.AuthResult;

import teamdevhub.devhub.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.api.web.resolver.LoginUser;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.facade.AuthFacade;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/login")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        AuthResult authResult = authFacade.login(loginRequestDto.toLoginCommand());
        ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(authResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authResult.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issueAccessToken(authResult.accessToken()))
                );
    }

    @PostMapping("/reissue")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> refresh(@CookieValue("refreshToken") String refreshToken) {
        AuthResult authResult = authFacade.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        TokenResponseDto.issueAccessToken(authResult.accessToken())
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<DataApiResponseDto<Void>> logout(@LoginUser AuthenticatedUser authenticatedUser) {
        authFacade.logout(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.LOGOUT_SUCCESS
                )
        );
    }
}
