package teamdevhub.devhub.adapter.in.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailVerificationCommand;
import teamdevhub.devhub.adapter.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.ConfirmEmailVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.LoginRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.in.common.annotation.LoginUser;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.in.mail.EmailVerificationUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final EmailVerificationUseCase emailVerificationUseCase;

    @PostMapping("/email-verification")
    public ResponseEntity<ApiDataResponseVo<Void>> sendEmailVerification(@Valid @RequestBody EmailVerificationRequestDto emailVerificationRequestDto) {
        emailVerificationUseCase.sendEmailVerification(emailVerificationRequestDto);
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCode.EMAIL_VERIFICATION_SENT
                )
        );
    }

    @PostMapping("/email-verification/confirm")
    public ResponseEntity<ApiDataResponseVo<Void>> confirmEmailVerification(@Valid @RequestBody ConfirmEmailVerificationRequestDto confirmEmailVerificationRequestDto) {
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = ConfirmEmailVerificationCommand.of(confirmEmailVerificationRequestDto.getEmail(), confirmEmailVerificationRequestDto.getCode());
        emailVerificationUseCase.confirmEmailVerification(confirmEmailVerificationCommand);
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCode.EMAIL_VERIFICATION_SUCCESS
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponseVo<TokenResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginCommand loginCommand = LoginCommand.fromLoginRequestDto(loginRequestDto);
        LoginResponseDto loginResponseDto = authUseCase.login(loginCommand);
        ResponseCookie refreshCookie = CookieUtil.createRefreshTokenCookie(loginResponseDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, loginResponseDto.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiDataResponseVo.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issue(loginResponseDto.getAccessToken())
                        )
                );
    }

    @PostMapping("/login/{oauth}")
    public ResponseEntity<ApiDataResponseVo<Void>> loginWithOauth(@PathVariable String oauth, @RequestBody LoginRequestDto loginRequestDto) {
        // LoginResponseDto loginResponseDto = authUseCase.loginWithOauth(oauth);
        // ResponseCookie refreshCookie = CookieUtil.createRefreshTokenCookie(loginResponseDto.getRefreshToken());
        return ResponseEntity.ok()
                //.header(HttpHeaders.AUTHORIZATION, loginResponseDto.toAuthorizationHeader())
                //.header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiDataResponseVo.successWithoutData(
                                SuccessCode.LOGIN_SUCCESS
                                //TokenResponseDto.issue(loginResponseDto.getAccessToken())
                        )
                );
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiDataResponseVo<TokenResponseDto>> refresh(@CookieValue("refreshToken") String refreshToken) {
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        authUseCase.reissueAccessToken(refreshToken)
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponseVo<Void>> revoke(@LoginUser AuthenticatedUser authenticatedUser) {
        authUseCase.revoke(authenticatedUser.email());
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCode.LOGOUT_SUCCESS
                )
        );
    }

}
