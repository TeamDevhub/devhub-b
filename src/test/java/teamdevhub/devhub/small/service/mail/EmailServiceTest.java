package teamdevhub.devhub.small.service.mail;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;
import teamdevhub.devhub.common.enums.EmailTemplateType;
import teamdevhub.devhub.port.in.mail.command.ConfirmEmailVerificationCommand;
import teamdevhub.devhub.service.exception.BusinessRuleException;
import teamdevhub.devhub.service.mail.EmailService;
import teamdevhub.devhub.service.mail.EmailTemplateVariables;
import teamdevhub.devhub.small.mock.infrastructure.FakeEmailNotificationSender;
import teamdevhub.devhub.small.mock.provider.FakeDateTimeProvider;
import teamdevhub.devhub.small.mock.provider.FakeEmailVerificationCodeProvider;
import teamdevhub.devhub.small.mock.repository.FakeEmailVerificationRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailServiceTest {

    private EmailService emailService;
    private FakeEmailNotificationSender fakeEmailNotificationSender;
    private FakeEmailVerificationRepository fakeEmailVerificationRepository;
    private FakeEmailVerificationCodeProvider fakeEmailVerificationCodeProvider;
    private FakeDateTimeProvider fakeDateTimeProvider;

    @BeforeEach
    void init() {
        fakeEmailNotificationSender = new FakeEmailNotificationSender();
        fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        fakeEmailVerificationRepository = new FakeEmailVerificationRepository(fakeDateTimeProvider);
        fakeEmailVerificationCodeProvider = new FakeEmailVerificationCodeProvider("123456");

        emailService = new EmailService(
                fakeEmailNotificationSender,
                fakeEmailVerificationRepository,
                fakeEmailVerificationCodeProvider,
                fakeDateTimeProvider
        );
    }

    @Test
    void 이메일로_인증코드를_발송할_수_있다() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto("test@example.com");

        // when
        emailService.sendEmailVerification(emailVerificationRequestDto);

        // then
        assertThat(fakeEmailNotificationSender.getSentEmails()).hasSize(1);
        FakeEmailNotificationSender.SentEmail sentEmail = fakeEmailNotificationSender.getSentEmails().get(0);
        assertThat(sentEmail.getEmail()).isEqualTo("test@example.com");
        assertThat(sentEmail.getTemplateType()).isEqualTo(EmailTemplateType.EMAIL_VERIFICATION);
        assertThat(sentEmail.getVariable(EmailTemplateVariables.CODE)).isNotNull();
    }

    @Test
    void 아직_이메일인증을_하지_않은_상태에서_다시_발송요청을_하면_예외를_던진다() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto("test@example.com");
        emailService.sendEmailVerification(emailVerificationRequestDto);

        // then
        assertThrows(BusinessRuleException.class, () ->
                // when
                emailService.sendEmailVerification(new EmailVerificationRequestDto("test@example.com"))
        );
    }

    @Test
    void 올바른_이메일_인증코드를_입력하면_검증완료여부_메서드_호출_시_true_가_반환된다() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto("test@example.com");
        emailService.sendEmailVerification(emailVerificationRequestDto);
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = new ConfirmEmailVerificationCommand("test@example.com", "123456");

        // when
        emailService.confirmEmailVerification(confirmEmailVerificationCommand);

        // then
        assertThat(fakeEmailVerificationRepository.findByEmail(confirmEmailVerificationCommand.getEmail()).isVerified()).isTrue();
    }


    @Test
    void 잘못된_이메일_인증코드를_입력하면_예외가_발생하며_추후_검증완료여부_메서드_호출_시_false_가_반환된다() {
        // given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto("test@example.com");
        emailService.sendEmailVerification(emailVerificationRequestDto);
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = new ConfirmEmailVerificationCommand("test@example.com", "654321");

        // then
        assertThrows(BusinessRuleException.class, () ->
                // when
                emailService.confirmEmailVerification(confirmEmailVerificationCommand)
        );
        assertThat(emailService.isVerified("test@example.com")).isFalse();
    }
}