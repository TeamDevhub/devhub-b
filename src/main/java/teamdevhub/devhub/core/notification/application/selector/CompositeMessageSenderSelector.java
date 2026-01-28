package teamdevhub.devhub.core.notification.application.selector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.core.notification.port.out.NotificationSender;
import teamdevhub.devhub.outbound.common.exception.ExternalServiceException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.core.auth.domain.vo.VerificationMessage;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeMessageSenderSelector implements NotificationSenderSelector {

    private final List<NotificationSender> notificationSenderList;

    public void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage) {
        findMessageSender(verificationTarget)
                .sendVerification(verificationTarget, verificationMessage);
    }

    private NotificationSender findMessageSender(VerificationTarget verificationTarget) {
        return notificationSenderList.stream()
                .filter(sender -> sender.supports(verificationTarget))
                .findFirst()
                .orElseThrow(
                        () -> ExternalServiceException.of(ErrorCode.NOTIFICATION_SEND_FAIL));
    }
}