package teamdevhub.devhub.adapter.in.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.adapter.in.auth.AuthFacade;
import teamdevhub.devhub.adapter.in.auth.dto.request.ConfirmVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.IssueVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.response.OauthCallbackResponseDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;

import java.io.IOException;

@RestController
@RequestMapping("/auth/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final AuthFacade authFacade;

    @PostMapping("/email")
    public ResponseEntity<DataApiResponseDto<Void>> sendEmailVerification(@Valid @RequestBody IssueVerificationRequestDto issueVerificationRequestDto) {
        authFacade.issueEmailVerification(issueVerificationRequestDto.toIssueVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.VERIFICATION_SENT
                )
        );
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<DataApiResponseDto<Void>> confirmEmailVerification(@Valid @RequestBody ConfirmVerificationRequestDto confirmVerificationRequestDto) {
        authFacade.confirmEmailVerification(confirmVerificationRequestDto.toConfirmVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.VERIFICATION_SUCCESS
                )
        );
    }

    @GetMapping("/oauth/{provider}")
    public void redirectToProvider(@PathVariable String provider, HttpServletResponse httpServletResponse) throws IOException {
        String authorizationUrl = authFacade.createOAuthAuthorizationUrl(provider);
        httpServletResponse.sendRedirect(authorizationUrl);
    }

    @GetMapping("/oauth/{provider}/callback")
    public ResponseEntity<DataApiResponseDto<OauthCallbackResponseDto>> handleOauthCallback(@PathVariable String provider, @RequestParam String code) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        authFacade.handleOAuthCallback(provider, code)
                )
        );
    }
}
