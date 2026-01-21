package teamdevhub.devhub.application.selector.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.adapter.out.exception.ExternalServiceException;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.out.sender.MessageSender;

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