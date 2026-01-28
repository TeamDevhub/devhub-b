package teamdevhub.devhub.core.notification.application.selector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.notification.port.out.MessageSender;
import teamdevhub.devhub.shared.exception.ExternalServiceException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeMessageSenderSelector implements NotificationSenderSelector {

    private final List<MessageSender> messageSenderList;

    public void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage) {
        findMessageSender(verificationTarget)
                .sendVerification(verificationTarget, verificationMessage);
    }

    private MessageSender findMessageSender(VerificationTarget verificationTarget) {
        return messageSenderList.stream()
                .filter(sender -> sender.supports(verificationTarget))
                .findFirst()
                .orElseThrow(
                        () -> ExternalServiceException.of(ErrorCode.NOTIFICATION_SEND_FAIL));
    }
}