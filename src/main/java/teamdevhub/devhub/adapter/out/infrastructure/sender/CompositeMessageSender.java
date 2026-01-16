package teamdevhub.devhub.adapter.out.infrastructure.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.out.sender.NotificationSender;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeMessageSender implements NotificationSender {

    private final List<MessageSender> messageSenderList;

    public void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage) {
        findMessageSender(verificationTarget).sendVerification(verificationTarget, verificationMessage);
    }

    private MessageSender findMessageSender(VerificationTarget verificationTarget) {
        return messageSenderList.stream()
                .filter(sender -> sender.supports(verificationTarget))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException("No MessageSender for " + verificationTarget.type()));
    }
}