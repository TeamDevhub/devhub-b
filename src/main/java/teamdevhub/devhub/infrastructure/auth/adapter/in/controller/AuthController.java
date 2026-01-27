package teamdevhub.devhub.infrastructure.auth.adapter.in.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.infrastructure.auth.adapter.in.dto.request.LoginRequestDto;
import teamdevhub.devhub.core.auth.application.service.vo.AuthResult;
import teamdevhub.devhub.infrastructure.auth.adapter.in.dto.response.TokenResponseDto;
import teamdevhub.devhub.shared.web.model.response.DataApiResponse;
import teamdevhub.devhub.shared.web.resolver.LoginUser;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.auth.port.in.facade.AuthFacade;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;

    @PostMapping("/login")
    public ResponseEntity<DataApiResponse<TokenResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        AuthResult authResult = authFacade.login(loginRequestDto.toCommand());
        ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(authResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authResult.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponse.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issueAccessToken(authResult.accessToken()))
                );
    }

    @PostMapping("/reissue")
    public ResponseEntity<DataApiResponse<TokenResponseDto>> refresh(@CookieValue("refreshToken") String refreshToken) {
        AuthResult authResult = authFacade.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(
                DataApiResponse.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        TokenResponseDto.issueAccessToken(authResult.accessToken())
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<DataApiResponse<Void>> logout(@LoginUser AuthenticatedUser authenticatedUser) {
        authFacade.logout(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                DataApiResponse.successWithoutData(
                        SuccessCode.LOGOUT_SUCCESS
                )
        );
    }
}
