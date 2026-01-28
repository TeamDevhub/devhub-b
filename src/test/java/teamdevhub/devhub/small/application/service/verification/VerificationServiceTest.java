package teamdevhub.devhub.small.application.service.verification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.auth.application.selector.VerificationIssuerSelector;
import teamdevhub.devhub.core.auth.application.service.verification.VerificationService;
import teamdevhub.devhub.core.auth.domain.Verification;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.core.auth.port.in.command.ConfirmVerificationCommand;
import teamdevhub.devhub.core.auth.port.in.command.IssueVerificationCommand;
import teamdevhub.devhub.core.auth.port.out.VerificationIssuer;
import teamdevhub.devhub.core.common.provider.TimeProvider;
import teamdevhub.devhub.fake.pure.issuer.FakeEmailVerificationIssuer;
import teamdevhub.devhub.fake.pure.provider.FakeTimeProvider;
import teamdevhub.devhub.fake.pure.repository.verification.FakeVerificationRepository;
import teamdevhub.devhub.fake.pure.selector.FakeVerificationIssuerSelector;
import teamdevhub.devhub.shared.exception.BusinessRuleException;
import teamdevhub.devhub.shared.exception.DomainRuleException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_CODE;

public class VerificationServiceTest {

    private VerificationService verificationService;

    private FakeVerificationRepository verificationRepository;

    @BeforeEach
    void init() {
        TimeProvider timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        VerificationIssuer verificationIssuer = new FakeEmailVerificationIssuer(VerificationType.EMAIL, TEST_EMAIL_CODE, timeProvider);
        VerificationIssuerSelector issuerSelector = new FakeVerificationIssuerSelector(List.of(verificationIssuer));
        verificationRepository = new FakeVerificationRepository();

        verificationService = new VerificationService(
                timeProvider,
                issuerSelector,
                verificationRepository
        );
    }

    @Test
    @DisplayName("인증을_발급하면_저장된다")
    void issueVerificationSuccess() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        IssueVerificationCommand command = new IssueVerificationCommand(verificationTarget);

        // when
        verificationService.issueVerification(command);

        // then
        Verification verification = verificationRepository.findByVerificationTarget(verificationTarget);

        assertThat(verification).isNotNull();
    }

    @Test
    @DisplayName("올바른_인증코드로_인증_확인에_성공한다")
    void confirmVerificationSuccess() {
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
    @DisplayName("인증되지_않은_상태에서_assertAllowed_호출_시_예외가_발생한다")
    void assertAllowedFailWhenNotConfirmed() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        verificationService.issueVerification(new IssueVerificationCommand(verificationTarget));

        // when & then
        assertThatThrownBy(
                () -> verificationService.assertAllowed(verificationTarget))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("인증_완료_후_assertAllowed_통과한다")
    void assertAllowedSuccess() {
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
