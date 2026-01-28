package teamdevhub.devhub.api.auth.adapter.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.api.auth.adapter.in.model.request.ConfirmVerificationRequestDto;
import teamdevhub.devhub.api.auth.adapter.in.model.request.IssueVerificationRequestDto;
import teamdevhub.devhub.core.auth.port.in.facade.VerificationFacade;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.shared.web.model.response.DataApiResponseDto;

@RestController
@RequestMapping("/auth/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationFacade verificationFacade;

    @PostMapping("/email")
    public ResponseEntity<DataApiResponseDto<Void>> sendEmailVerification(@Valid @RequestBody IssueVerificationRequestDto issueVerificationRequestDto) {
        verificationFacade.issueVerification(issueVerificationRequestDto.toIssueVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.VERIFICATION_SENT
                )
        );
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<DataApiResponseDto<Void>> confirmEmailVerification(@Valid @RequestBody ConfirmVerificationRequestDto confirmVerificationRequestDto) {
        verificationFacade.confirmVerification(confirmVerificationRequestDto.toConfirmVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.VERIFICATION_SUCCESS
                )
        );
    }
}
