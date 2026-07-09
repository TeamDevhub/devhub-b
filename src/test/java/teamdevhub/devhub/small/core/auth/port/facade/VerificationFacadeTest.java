package teamdevhub.devhub.small.core.auth.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.core.auth.port.in.command.verification.ConfirmVerificationCommand;
import teamdevhub.devhub.core.auth.port.in.command.verification.IssueVerificationCommand;
import teamdevhub.devhub.core.auth.port.in.facade.VerificationFacade;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.verification.FakeVerificationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.notification.FakeNotificationUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class VerificationFacadeTest {

    private VerificationFacade verificationFacade;

    private FakeVerificationUseCase verificationUseCase;
    private FakeNotificationUseCase notificationUseCase;

    @BeforeEach
    void init() {
        verificationUseCase = new FakeVerificationUseCase();
        notificationUseCase = new FakeNotificationUseCase();

        verificationFacade = new VerificationFacade(verificationUseCase, notificationUseCase);
    }

    @Test
    @DisplayName("issueVerification_은_인증을_발급하고_알림을_전송한다")
    void issueVerification_issuesAndSendsNotification() {
        // given
        VerificationTarget target = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);
        IssueVerificationCommand issueVerificationCommand = new IssueVerificationCommand(target);

        // when
        verificationFacade.issueVerification(issueVerificationCommand);

        // then
        assertThat(notificationUseCase.getSentVerifications()).hasSize(1);
    }

    @Test
    @DisplayName("confirmVerification_은_인증_코드를_검증한다")
    void confirmVerification_confirmsCode() {
        // given
        VerificationTarget target = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);
        IssueVerificationCommand issueCommand = new IssueVerificationCommand(target);
        verificationFacade.issueVerification(issueCommand);

        ConfirmVerificationCommand confirmCommand = new ConfirmVerificationCommand(target, TEST_EMAIL_CODE);

        // when, then
        verificationFacade.confirmVerification(confirmCommand);
    }
}
