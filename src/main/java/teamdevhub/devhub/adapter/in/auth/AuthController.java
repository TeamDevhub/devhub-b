package teamdevhub.devhub.adapter.in.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailCertificationCommand;
import teamdevhub.devhub.adapter.in.auth.command.LoginCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.ConfirmEmailCertificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailCertificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.LoginRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.LoginResponseDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.TokenResponseDto;
import teamdevhub.devhub.adapter.in.common.annotation.CurrentUser;
import teamdevhub.devhub.adapter.in.common.vo.ApiDataResponseVo;
import teamdevhub.devhub.common.enums.SuccessCodeEnum;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.in.mail.EmailCertificationUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final EmailCertificationUseCase emailCertificationUseCase;

    @PostMapping("/email-certification")
    public ResponseEntity<ApiDataResponseVo<Void>> sendEmailCertificationCode(@Valid @RequestBody EmailCertificationRequestDto emailCertificationRequestDto) {
        emailCertificationUseCase.sendEmailCertificationCode(emailCertificationRequestDto);
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.EMAIL_CERTIFICATION_SENT
                )
        );
    }

    @PostMapping("/email-certification/confirm")
    public ResponseEntity<ApiDataResponseVo<Void>> confirmEmailCertificationCode(@Valid @RequestBody ConfirmEmailCertificationRequestDto confirmEmailCertificationRequestDto) {
        ConfirmEmailCertificationCommand confirmEmailCertificationCommand = ConfirmEmailCertificationCommand.of(confirmEmailCertificationRequestDto.getEmail(), confirmEmailCertificationRequestDto.getCode());
        emailCertificationUseCase.confirmEmailCertificationCode(confirmEmailCertificationCommand);
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.EMAIL_CERTIFICATION_SUCCESS
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
                        SuccessCodeEnum.LOGIN_SUCCESS,
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
                                SuccessCodeEnum.LOGIN_SUCCESS
                                //TokenResponseDto.issue(loginResponseDto.getAccessToken())
                        )
                );
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiDataResponseVo<TokenResponseDto>> refresh(@CookieValue("refreshToken") String refreshToken) {
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithData(
                        SuccessCodeEnum.CREATE_SUCCESS,
                        authUseCase.reissueAccessToken(refreshToken)
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponseVo<Void>> revoke(@CurrentUser AuthenticatedUser authenticatedUser) {
        authUseCase.revoke(authenticatedUser.email());
        return ResponseEntity.ok(
                ApiDataResponseVo.successWithoutData(
                        SuccessCodeEnum.LOGOUT_SUCCESS
                )
        );
    }

}
