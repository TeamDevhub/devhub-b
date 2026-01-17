package teamdevhub.devhub.fake.pure.usecase.verification;

import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.port.out.provider.TimeProvider;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.fake.pure.provider.FakeTimeProvider;
import teamdevhub.devhub.port.in.verification.VerificationUseCase;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_CODE;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;

public class FakeVerificationUseCase implements VerificationUseCase {

    private final Map<String, Verification> store = new HashMap<>();
    private final TimeProvider timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));

    public FakeVerificationUseCase() {
        VerificationTarget target = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        Verification verification = Verification.issue(target, TEST_EMAIL_CODE, timeProvider.now().plusMinutes(5));
        verification.confirm(TEST_EMAIL_CODE, timeProvider.now());
        store.put(target.value(), verification);
    }

    @Override
    public void issueVerification(IssueVerificationCommand issueVerificationCommand) {
        VerificationTarget verificationTarget = issueVerificationCommand.verificationTarget();
        Verification verification = Verification.issue(verificationTarget, TEST_EMAIL_CODE, timeProvider.now().plusMinutes(5));
        store.put(verificationTarget.value(), verification);
    }

    @Override
    public void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        VerificationTarget verificationTarget = confirmVerificationCommand.verificationTarget();
        Verification verification = store.get(verificationTarget.value());
        verification.confirm(confirmVerificationCommand.code(), timeProvider.now());
        store.put(verificationTarget.value(), verification);
    }

    @Override
    public void assertAllowed(VerificationTarget verificationTarget) {
        Verification verification = store.get(verificationTarget.value());
        verification.assertValid(timeProvider.now());
    }

    @Override
    public void consume(VerificationTarget verificationTarget) {
        store.remove(verificationTarget.value());
    }

    public void putUnverified(VerificationTarget verificationTarget) {
        Verification verification = Verification.issue(verificationTarget, TEST_EMAIL_CODE, timeProvider.now().plusMinutes(5));
        store.put(verificationTarget.value(), verification);
    }
}
