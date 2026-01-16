package teamdevhub.devhub.fake.pure.usecase;

import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.fake.pure.provider.FakeDateTimeProvider;
import teamdevhub.devhub.port.in.verification.SignupVerificationUseCase;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FakeSignupVerificationUseCase implements SignupVerificationUseCase {

    private final Map<String, Verification> store = new HashMap<>();
    private final DateTimeProvider dateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));

    @Override
    public void issueSignupVerification(IssueVerificationCommand issueVerificationCommand) {
        VerificationTarget target = issueVerificationCommand.verificationTarget();
        Verification verification = Verification.issue(target, "FAKE-CODE", dateTimeProvider.now().plusMinutes(5));
        store.put(target.value(), verification);
    }

    @Override
    public void confirmSignupVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        VerificationTarget target = confirmVerificationCommand.verificationTarget();
        Verification verification = store.get(target.value());
        if (verification == null || !verification.verify(confirmVerificationCommand.code(), dateTimeProvider.now())) {
            throw new RuntimeException("Verification failed");
        }
        store.put(target.value(), verification);
    }

    @Override
    public void assertSignupAllowed(VerificationTarget target) {
        Verification verification = store.get(target.value());
        if (verification == null || !verification.isVerified() || verification.isExpired(dateTimeProvider.now())) {
            throw new RuntimeException("Signup not allowed");
        }
    }

    @Override
    public void consume(VerificationTarget verificationTarget) {
        store.remove(verificationTarget.value());
    }

    @Override
    public void validateSignupVerification(VerificationTarget verificationTarget) {
        Verification verification = store.get(verificationTarget.value());
        if (verification == null || !verification.isVerified()) {
            throw new RuntimeException("Verification not valid");
        }
    }

    public Verification getVerification(VerificationTarget target) {
        return store.get(target.value());
    }
}
