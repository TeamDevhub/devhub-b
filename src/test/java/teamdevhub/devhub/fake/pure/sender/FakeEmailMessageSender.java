package teamdevhub.devhub.fake.pure.sender;

import teamdevhub.devhub.adapter.out.infrastructure.sender.MessageSender;
import teamdevhub.devhub.domain.verification.vo.VerificationMessage;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;

import java.util.ArrayList;
import java.util.List;

public class FakeEmailMessageSender implements MessageSender {

    private final List<String> sentEmails = new ArrayList<>();

    @Override
    public boolean supports(VerificationTarget verificationTarget) {
        return verificationTarget.verificationType() == VerificationType.EMAIL;
    }

    @Override
    public void sendVerification(VerificationTarget verificationTarget, VerificationMessage verificationMessage) {
        sentEmails.add(verificationTarget.value());
    }

    public boolean wasSentTo(String email) {
        return sentEmails.contains(email);
    }
}
