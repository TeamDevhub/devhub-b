package teamdevhub.devhub.adapter.out.common.sender;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.domain.verification.VerificationMessage;
import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.port.out.sender.MessageSender;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeMessageSender {

    private final List<MessageSender> senders;

    public void sendVerification(VerificationTarget target, VerificationMessage message) {
        for (MessageSender sender : senders) {
            if (sender.supports(target)) {
                sender.sendVerification(target, message);
                return;
            }
        }

        throw new IllegalStateException(
                "No MessageSender for target type: " + target.type()
        );
    }
}