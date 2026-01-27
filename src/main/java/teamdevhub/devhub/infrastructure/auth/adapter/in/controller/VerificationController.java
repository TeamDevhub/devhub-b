package teamdevhub.devhub.infrastructure.auth.adapter.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.infrastructure.auth.adapter.in.dto.request.ConfirmVerificationRequestDto;
import teamdevhub.devhub.infrastructure.auth.adapter.in.dto.request.IssueVerificationRequestDto;
import teamdevhub.devhub.shared.web.model.response.DataApiResponse;
import teamdevhub.devhub.shared.enums.SuccessCode;
import teamdevhub.devhub.core.auth.port.in.usecase.VerificationUseCase;

@RestController
@RequestMapping("/auth/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationUseCase verificationUseCase;

    @PostMapping("/email")
    public ResponseEntity<DataApiResponse<Void>> sendEmailVerification(@Valid @RequestBody IssueVerificationRequestDto issueVerificationRequestDto) {
        verificationUseCase.issueVerification(issueVerificationRequestDto.toIssueVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponse.successWithoutData(
                        SuccessCode.VERIFICATION_SENT
                )
        );
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<DataApiResponse<Void>> confirmEmailVerification(@Valid @RequestBody ConfirmVerificationRequestDto confirmVerificationRequestDto) {
        verificationUseCase.confirmVerification(confirmVerificationRequestDto.toConfirmVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponse.successWithoutData(
                        SuccessCode.VERIFICATION_SUCCESS
                )
        );
    }
}
