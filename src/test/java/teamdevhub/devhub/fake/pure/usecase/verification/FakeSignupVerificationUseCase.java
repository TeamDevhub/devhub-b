package teamdevhub.devhub.fake.pure.usecase.verification;

import teamdevhub.devhub.port.out.provider.TimeProvider;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.fake.pure.provider.FakeTimeProvider;
import teamdevhub.devhub.port.in.verification.SignupVerificationUseCase;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FakeSignupVerificationUseCase implements SignupVerificationUseCase {

    private final Map<String, Verification> store = new HashMap<>();
    private final TimeProvider timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));

    @Override
    public void issueSignupVerification(IssueVerificationCommand issueVerificationCommand) {
        VerificationTarget target = issueVerificationCommand.verificationTarget();
        Verification verification = Verification.issue(target, "FAKE-CODE", timeProvider.now().plusMinutes(5));
        store.put(target.value(), verification);
    }

    @Override
    public void confirmSignupVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        VerificationTarget target = confirmVerificationCommand.verificationTarget();
        Verification verification = store.get(target.value());
        verification.confirm(confirmVerificationCommand.code(), timeProvider.now());
        store.put(target.value(), verification);
    }

    @Override
    public void assertSignupAllowed(VerificationTarget target) {
        Verification verification = store.get(target.value());
        verification.assertValid(timeProvider.now());
    }

    @Override
    public void consume(VerificationTarget verificationTarget) {
        store.remove(verificationTarget.value());
    }
}
