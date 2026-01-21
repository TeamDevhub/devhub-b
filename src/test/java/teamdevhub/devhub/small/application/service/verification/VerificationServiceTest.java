package teamdevhub.devhub.small.application.service.verification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.exception.BusinessRuleException;
import teamdevhub.devhub.application.service.verification.VerificationService;
import teamdevhub.devhub.port.out.selector.VerificationIssuerSelector;
import teamdevhub.devhub.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.fake.pure.issuer.FakeEmailVerificationIssuer;
import teamdevhub.devhub.fake.pure.issuer.FakeVerificationIssuerSelector;
import teamdevhub.devhub.fake.pure.provider.FakeTimeProvider;
import teamdevhub.devhub.fake.pure.repository.verification.FakeVerificationRepository;
import teamdevhub.devhub.fake.pure.sender.FakeNotificationSenderSelector;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;
import teamdevhub.devhub.port.out.provider.TimeProvider;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class VerificationServiceTest {

    private FakeNotificationSenderSelector notificationSender;
    private FakeVerificationRepository verificationRepository;

    private VerificationService verificationService;

    @BeforeEach
    void init() {
        TimeProvider timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));

        VerificationIssuer verificationIssuer = new FakeEmailVerificationIssuer(VerificationType.EMAIL, TEST_EMAIL_CODE, timeProvider);
        VerificationIssuerSelector issuerSelector = new FakeVerificationIssuerSelector(List.of(verificationIssuer));
        verificationRepository = new FakeVerificationRepository();
        notificationSender = new FakeNotificationSenderSelector();

        verificationService = new VerificationService(
                timeProvider,
                issuerSelector,
                notificationSender,
                verificationRepository
        );
    }

    @Test
    @DisplayName("인증을 발급하면 저장되고 알림이 전송된다")
    void issueVerification_success() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        IssueVerificationCommand command = new IssueVerificationCommand(verificationTarget);

        // when
        verificationService.issueVerification(command);

        // then
        Verification verification = verificationRepository.findByVerificationTarget(verificationTarget);

        assertThat(verification).isNotNull();
        assertThat(notificationSender.isSent()).isTrue();
    }

    @Test
    @DisplayName("올바른 코드로 인증 확인에 성공한다")
    void confirmVerification_success() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);
        verificationService.issueVerification(new IssueVerificationCommand(verificationTarget));

        // when
        verificationService.confirmVerification(new ConfirmVerificationCommand(verificationTarget, TEST_EMAIL_CODE));

        // then
        Verification verification = verificationRepository.findByVerificationTarget(verificationTarget);

        assertThat(verification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("인증되지 않은 상태에서 assertAllowed 호출 시 예외")
    void assertAllowed_fail_whenNotConfirmed() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        verificationService.issueVerification(new IssueVerificationCommand(verificationTarget));

        // when & then
        assertThatThrownBy(
                () -> verificationService.assertAllowed(verificationTarget))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("인증 완료 후 assertAllowed 통과")
    void assertAllowed_success() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        verificationService.issueVerification(new IssueVerificationCommand(verificationTarget));

        verificationService.confirmVerification(new ConfirmVerificationCommand(verificationTarget, TEST_EMAIL_CODE));

        // when & then
        assertThatCode(
                () -> verificationService.assertAllowed(verificationTarget))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("consume 호출 시 인증 정보가 삭제된다")
    void consume_success() {
        // given
        VerificationTarget target = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        verificationService.issueVerification(new IssueVerificationCommand(target));

        // when
        verificationService.consume(target);

        // then
        assertThatThrownBy(
                () -> verificationRepository.findByVerificationTarget(target))
                .isInstanceOf(BusinessRuleException.class);
    }
}
