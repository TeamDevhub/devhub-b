package teamdevhub.devhub.small.service.mail;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.auth.command.ConfirmEmailVerificationCommand;
import teamdevhub.devhub.adapter.in.auth.dto.request.EmailVerificationRequestDto;
import teamdevhub.devhub.common.exception.AuthRuleException;
import teamdevhub.devhub.common.enums.EmailTemplateType;
import teamdevhub.devhub.service.common.exception.BusinessRuleException;
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
    private FakeEmailNotificationSender fakeEmailSender;
    private FakeEmailVerificationRepository fakeRepository;
    private FakeEmailVerificationCodeProvider fakeCodeProvider;
    private FakeDateTimeProvider fakeDateTimeProvider;

    @BeforeEach
    void init() {
        fakeEmailSender = new FakeEmailNotificationSender();
        fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        fakeRepository = new FakeEmailVerificationRepository(fakeDateTimeProvider);
        fakeCodeProvider = new FakeEmailVerificationCodeProvider("123456");

        emailService = new EmailService(
                fakeEmailSender,
                fakeRepository,
                fakeCodeProvider,
                fakeDateTimeProvider
        );
    }

    @Test
    void 이메일로_인증코드를_발송할_수_있다() {
        //given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto("test@example.com");

        //when
        emailService.sendEmailCertificationCode(emailVerificationRequestDto);

        //then
        assertThat(fakeEmailSender.getSentEmails()).hasSize(1);
        FakeEmailNotificationSender.SentEmail sentEmail = fakeEmailSender.getSentEmails().get(0);
        assertThat(sentEmail.getEmail()).isEqualTo("test@example.com");
        assertThat(sentEmail.getTemplateType()).isEqualTo(EmailTemplateType.EMAIL_CERTIFICATION);
        assertThat(sentEmail.getVariable(EmailTemplateVariables.CODE)).isNotNull();
    }

    @Test
    void 아직_이메일인증을_하지_않은_상태에서_다시_발송요청을_하면_예외를_던진다() {
        //given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto("test@example.com");
        emailService.sendEmailCertificationCode(emailVerificationRequestDto);

        //then
        assertThrows(AuthRuleException.class, () ->
                //when
                emailService.sendEmailCertificationCode(new EmailVerificationRequestDto("test@example.com"))
        );
    }

    @Test
    void 올바른_이메일_인증코드를_입력하면_검증완료여부_메서드_호출_시_true_가_반환된다() {
        //given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto("test@example.com");
        emailService.sendEmailCertificationCode(emailVerificationRequestDto);
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = new ConfirmEmailVerificationCommand("test@example.com", "123456");

        //when
        emailService.confirmEmailCertificationCode(confirmEmailVerificationCommand);

        //then
        assertThat().isTrue();
    }

    //@Test
    //emailService.isVerified("test@example.com")

    @Test
    void 잘못된_이메일_인증코드를_입력하면_예외가_발생하며_추후_검증완료여부_메서드_호출_시_false_가_반환된다() {
        //given
        EmailVerificationRequestDto emailVerificationRequestDto = new EmailVerificationRequestDto("test@example.com");
        emailService.sendEmailCertificationCode(emailVerificationRequestDto);
        ConfirmEmailVerificationCommand confirmEmailVerificationCommand = new ConfirmEmailVerificationCommand("test@example.com", "654321");

        //then
        assertThrows(BusinessRuleException.class, () ->
                //when
                emailService.confirmEmailCertificationCode(confirmEmailVerificationCommand)
        );
        assertThat(emailService.isVerified("test@example.com")).isFalse();
    }
}