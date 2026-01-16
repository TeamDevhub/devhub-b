package teamdevhub.devhub.service.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.vo.IssuedVerification;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.verification.SignupVerificationUseCase;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;
import teamdevhub.devhub.port.out.sender.NotificationSender;
import teamdevhub.devhub.port.out.verification.VerificationManager;
import teamdevhub.devhub.port.out.verification.VerificationRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationService implements SignupVerificationUseCase {

    private final VerificationManager verificationManager;
    private final NotificationSender notificationSender;
    private final DateTimeProvider dateTimeProvider;
    private final VerificationRepository verificationRepository;

    @Override
    public void issueSignupVerification(IssueVerificationCommand issueVerificationCommand) {
        IssuedVerification issuedVerification = verificationManager.issueVerification(issueVerificationCommand.verificationTarget());

        verificationRepository.save(issuedVerification.getVerification());

        issuedVerification.getVerificationMessage()
                .ifPresent(verificationMessage -> notificationSender.sendVerification(issueVerificationCommand.verificationTarget(), verificationMessage));
    }

    @Override
    public void confirmSignupVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        Verification verification = verificationRepository.findByVerificationTarget(confirmVerificationCommand.verificationTarget());

        boolean success = verification.verify(confirmVerificationCommand.code(), dateTimeProvider.now());

        if (!success) {
            throw DomainRuleException.of(ErrorCode.AUTH_FAIL);
        }

        verificationRepository.save(verification);
    }

    @Override
    public void assertSignupAllowed(VerificationTarget target) {
        Verification verification = verificationRepository.findByVerificationTarget(target);
        if (!verification.isVerified() || verification.isExpired(dateTimeProvider.now())) {
            throw DomainRuleException.of(ErrorCode.AUTH_FAIL);
        }
    }

    @Override
    public void consume(VerificationTarget verificationTarget) {
        verificationRepository.deleteByVerificationTarget(verificationTarget);
    }

    @Override
    public void validateSignupVerification(VerificationTarget verificationTarget) {
        Verification verification = verificationRepository.findByVerificationTarget(verificationTarget);

        if (!verification.isVerified()) {
            throw DomainRuleException.of(ErrorCode.AUTH_FAIL);
        }
    }
}
