package teamdevhub.devhub.medium.adapter.in.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.adapter.in.auth.controller.VerificationController;
import teamdevhub.devhub.adapter.in.auth.dto.request.ConfirmVerificationRequestDto;
import teamdevhub.devhub.adapter.in.auth.dto.request.IssueVerificationRequestDto;
import teamdevhub.devhub.adapter.in.web.dto.response.DataApiResponseDto;
import teamdevhub.devhub.common.enums.SuccessCode;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.port.in.verification.usecase.VerificationUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_CODE;

public class VerificationControllerTest {

    private VerificationController verificationController;

    private VerificationUseCase verificationUseCase;

    @BeforeEach
    void init() {
        verificationUseCase = Mockito.mock(VerificationUseCase.class);

        verificationController = new VerificationController(verificationUseCase);
    }

    @Test
    @DisplayName("이메일_인증_메일_전송에_성공하면_VERIFICATION_SENT_코드를_확인할_수_있다")
    void canVerifyCodeWhenSendingEmailVerification() {
        // given
        IssueVerificationRequestDto issueVerificationRequestDto = new IssueVerificationRequestDto(VerificationType.EMAIL, TEST_EMAIL_1);
        doNothing().when(verificationUseCase).issueVerification(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = verificationController.sendEmailVerification(issueVerificationRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.VERIFICATION_SENT.getCode());

        verify(verificationUseCase).issueVerification(any());
    }

    @Test
    @DisplayName("이메일_인증_확인에_성공하면_VERIFICATION_SUCCESS_코드를_확인할_수_있다")
    void canVerifyCodeWhenConfirmingEmailVerification() {
        // given
        ConfirmVerificationRequestDto confirmVerificationRequestDto = new ConfirmVerificationRequestDto(
                VerificationType.EMAIL,
                TEST_EMAIL_1,
                TEST_EMAIL_CODE
        );
        doNothing().when(verificationUseCase).confirmVerification(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = verificationController.confirmEmailVerification(confirmVerificationRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.VERIFICATION_SUCCESS.getCode());

        verify(verificationUseCase).confirmVerification(any());
    }
}
