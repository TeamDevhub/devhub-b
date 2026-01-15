package teamdevhub.devhub.small.service.mail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.dto.request.auth.EmailVerificationRequestDto;
import teamdevhub.devhub.common.enums.EmailTemplateType;
import teamdevhub.devhub.port.in.mail.command.ConfirmEmailVerificationCommand;
import teamdevhub.devhub.service.exception.BusinessRuleException;
import teamdevhub.devhub.service.mail.EmailService;
import teamdevhub.devhub.service.mail.EmailTemplateVariables;
import teamdevhub.devhub.fake.pure.external.FakeEmailNotificationSender;
import teamdevhub.devhub.fake.pure.provider.FakeDateTimeProvider;
import teamdevhub.devhub.fake.pure.provider.FakeEmailVerificationCodeProvider;
import teamdevhub.devhub.fake.pure.repository.FakeEmailVerificationRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class EmailServiceTest {

    private EmailService emailService;
    private FakeEmailNotificationSender fakeEmailNotificationSender;
    private FakeEmailVerificationRepository fakeEmailVerificationRepository;

    @BeforeEach
    void init() {
        fakeEmailNotificationSender = new FakeEmailNotificationSender();
        FakeDateTimeProvider fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        fakeEmailVerificationRepository = new FakeEmailVerificationRepository(fakeDateTimeProvider);
        FakeEmailVerificationCodeProvider fakeEmailVerificationCodeProvider = new FakeEmailVerificationCodeProvider("123456");

        emailService = new EmailService(
                fakeEmailNotificationSender,
                fakeEmailVerificationRepository,
                fakeEmailVerificationCodeProvider,
                fakeDateTimeProvider
        );
    }

    @Test
    @DisplayName("이메일로_인증코드를_발송할_수_있다")
    void sendVerificationCodeByEmail() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto(TEST_EMAIL_1);

        // when
        emailService.sendEmailVerification(emailVerificationRequestDto);

        // then
        assertThat(fakeEmailNotificationSender.getSentEmails()).hasSize(1);
        FakeEmailNotificationSender.SentEmail sentEmail = fakeEmailNotificationSender.getSentEmails().get(0);
        assertThat(sentEmail.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(sentEmail.templateType()).isEqualTo(EmailTemplateType.EMAIL_VERIFICATION);
        assertThat(sentEmail.getVariable(EmailTemplateVariables.CODE)).isNotNull();
    }

    @Test
    @DisplayName("아직_이메일인증을_하지_않은_상태에서_다시_발송요청을_하면_예외를_던진다")
    void throwExceptionWhenResendingUnverifiedEmailCode() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto(TEST_EMAIL_1);
        emailService.sendEmailVerification(emailVerificationRequestDto);

        // then
        assertThrows(BusinessRuleException.class,
                // when
                () -> emailService.sendEmailVerification(new EmailVerificationRequestDto(TEST_EMAIL_1)));
    }

    @Test
    @DisplayName("올바른_이메일_인증코드를_입력하면_검증완료여부_메서드_호출_시_true_가_반환된다")
    void returnTrueWhenVerifyingWithCorrectEmailCode() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto(TEST_EMAIL_1);
        emailService.sendEmailVerification(emailVerificationRequestDto);
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = new ConfirmEmailVerificationCommand(TEST_EMAIL_1, EMAIL_CODE);

        // when
        emailService.confirmEmailVerification(confirmEmailVerificationCommand);

        // then
        assertThat(fakeEmailVerificationRepository.findByEmail(confirmEmailVerificationCommand.getEmail()).isVerified()).isTrue();
    }


    @Test
    @DisplayName("잘못된_이메일_인증코드를_입력하면_예외가_발생하며_추후_검증완료여부_메서드_호출_시_false_가_반환된다")
    void returnFalseWhenVerifyingWithIncorrectEmailCode() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto(TEST_EMAIL_1);
        emailService.sendEmailVerification(emailVerificationRequestDto);
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = new ConfirmEmailVerificationCommand(TEST_EMAIL_1, "654321");

        // then
        assertThrows(BusinessRuleException.class,
                // when
                () -> emailService.confirmEmailVerification(confirmEmailVerificationCommand));
        assertThat(emailService.isVerified(TEST_EMAIL_1)).isFalse();
    }
}