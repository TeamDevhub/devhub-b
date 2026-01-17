package teamdevhub.devhub.small.adapter.out.infrastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.exception.ExternalServiceException;
import teamdevhub.devhub.adapter.out.infrastructure.sender.CompositeMessageSender;
import teamdevhub.devhub.adapter.out.infrastructure.sender.MessageSender;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class CompositeMessageSenderTest {

    private CompositeMessageSender compositeMessageSender;
    private FakeMessageSender emailSender;


    static class FakeMessageSender implements MessageSender {
        private final VerificationType type;
        private boolean sent = false;
        private VerificationTarget lastTarget;
        private VerificationMessage lastMessage;

        public FakeMessageSender(VerificationType type) {
            this.type = type;
        }

        @Override
        public boolean supports(VerificationTarget verificationTarget) {
            return verificationTarget.verificationType() == type;
        }

        @Override
        public void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage) {
            this.sent = true;
            this.lastTarget = verificationTarget;
            this.lastMessage = verificationMessage;
        }

        public boolean isSent() {
            return sent;
        }

        public VerificationTarget getLastTarget() {
            return lastTarget;
        }

        public VerificationMessage getLastMessage() {
            return lastMessage;
        }
    }

    @BeforeEach
    void init() {
        emailSender = new FakeMessageSender(VerificationType.EMAIL);
        FakeMessageSender smsSender = new FakeMessageSender(VerificationType.SMS);
        compositeMessageSender = new CompositeMessageSender(List.of(emailSender, smsSender));
    }

    @Test
    @DisplayName("지원되는_VerificationTarget_이면_해당_MessageSender_가_호출된다")
    void sendVerificationCallsCorrectSender() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, null);

        // when
        compositeMessageSender.sendVerification(verificationTarget, verificationMessage);

        // then
        assertThat(emailSender.isSent()).isTrue();
        assertThat(emailSender.getLastTarget()).isEqualTo(verificationTarget);
        assertThat(emailSender.getLastMessage()).isEqualTo(verificationMessage);
    }

    @Test
    @DisplayName("지원하지_않는_VerificationTarget_이면_예외가_발생한다")
    void unsupportedVerificationTargetThrows() {
        // given
        VerificationTarget unsupportedVerificationTarget = VerificationTarget.of(VerificationType.OTP, "123456");
        VerificationMessage verificationMessage = new VerificationMessage("123456", null);

        // when, then
        assertThatThrownBy(() -> compositeMessageSender.sendVerification(unsupportedVerificationTarget, verificationMessage))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("발송이 실패했습니다.");
    }
}
