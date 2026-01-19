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
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.adapter.in.web.resolver.LoginUser;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;
import teamdevhub.devhub.port.in.auth.AuthSessionUseCase;
import teamdevhub.devhub.port.in.verification.VerificationUseCase;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthSessionUseCase authSessionUseCase;
    private final VerificationUseCase verificationUseCase;

    @PostMapping("/email-verification")
    public ResponseEntity<DataApiResponseDto<Void>> sendEmailVerification(@Valid @RequestBody IssueVerificationRequestDto issueVerificationRequestDto) {
        verificationUseCase.issueVerification(issueVerificationRequestDto.toIssueVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.VERIFICATION_SENT
                )
        );
    }

    @PostMapping("/email-verification/confirm")
    public ResponseEntity<DataApiResponseDto<Void>> confirmEmailVerification(@Valid @RequestBody ConfirmVerificationRequestDto confirmVerificationRequestDto) {
        verificationUseCase.confirmVerification(confirmVerificationRequestDto.toConfirmVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.VERIFICATION_SUCCESS
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authSessionUseCase.login(loginRequestDto.toCommand());
        ResponseCookie refreshCookie = CookieFactory.createRefreshTokenCookie(loginResponseDto.getRefreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, loginResponseDto.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issue(loginResponseDto.getAccessToken()))
                );
    }

    @PostMapping("/reissue")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> refresh(@CookieValue("refreshToken") String refreshToken) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        authSessionUseCase.reissueAccessToken(refreshToken)
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<DataApiResponseDto<Void>> revoke(@LoginUser AuthenticatedUser authenticatedUser) {
        authSessionUseCase.revoke(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.LOGOUT_SUCCESS
                )
        );
    }

}
