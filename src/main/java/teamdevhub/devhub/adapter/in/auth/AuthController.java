package teamdevhub.devhub.adapter.in.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.dto.request.auth.LoginRequestDto;
import teamdevhub.devhub.adapter.in.dto.request.verification.ConfirmVerificationRequestDto;
import teamdevhub.devhub.adapter.in.dto.request.verification.IssueVerificationRequestDto;
import teamdevhub.devhub.adapter.in.dto.response.auth.LoginResponseDto;
import teamdevhub.devhub.adapter.in.dto.response.auth.TokenResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.ApiDataResponseDto;
import teamdevhub.devhub.adapter.in.web.resolver.LoginUser;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.user.vo.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.AuthUseCase;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;
import teamdevhub.devhub.port.in.verification.SignupVerificationUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthUseCase authUseCase;
    private final SignupVerificationUseCase signupVerificationUseCase;

    @PostMapping("/email-verification")
    public ResponseEntity<ApiDataResponseDto<Void>> sendEmailVerification(@Valid @RequestBody IssueVerificationRequestDto issueVerificationRequestDto) {
        signupVerificationUseCase.issueSignupVerification(issueVerificationRequestDto.toIssueVerificationCommand());
        return ResponseEntity.ok(
                ApiDataResponseDto.successWithoutData(
                        SuccessCode.EMAIL_VERIFICATION_SENT
                )
        );
    }

    @PostMapping("/email-verification/confirm")
    public ResponseEntity<ApiDataResponseDto<Void>> confirmEmailVerification(@Valid @RequestBody ConfirmVerificationRequestDto confirmVerificationRequestDto) {
        signupVerificationUseCase.confirmSignupVerification(confirmVerificationRequestDto.toConfirmVerificationCommand());
        return ResponseEntity.ok(
                ApiDataResponseDto.successWithoutData(
                        SuccessCode.EMAIL_VERIFICATION_SUCCESS
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponseDto<TokenResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginCommand loginCommand = LoginCommand.fromLoginRequestDto(loginRequestDto);
        LoginResponseDto loginResponseDto = authUseCase.login(loginCommand);
        ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(loginResponseDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, loginResponseDto.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(ApiDataResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issue(loginResponseDto.getAccessToken())
                        )
                );
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiDataResponseDto<TokenResponseDto>> refresh(@CookieValue("refreshToken") String refreshToken) {
        return ResponseEntity.ok(
                ApiDataResponseDto.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        authUseCase.reissueAccessToken(refreshToken)
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponseDto<Void>> revoke(@LoginUser AuthenticatedUser authenticatedUser) {
        authUseCase.revoke(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                ApiDataResponseDto.successWithoutData(
                        SuccessCode.LOGOUT_SUCCESS
                )
        );
    }

}
