package teamdevhub.devhub.service.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.out.common.sender.CompositeMessageSender;
import teamdevhub.devhub.adapter.out.verification.CompositeVerificationIssuer;
import teamdevhub.devhub.common.enums.ErrorCode;
import teamdevhub.devhub.common.provider.datetime.DateTimeProvider;
import teamdevhub.devhub.domain.exception.DomainRuleException;
import teamdevhub.devhub.domain.verification.Verification;
import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.port.in.verification.SignupVerificationUseCase;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;
import teamdevhub.devhub.port.out.verification.VerificationRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationService implements SignupVerificationUseCase {

    private final VerificationRepository verificationRepository;
    private final CompositeVerificationIssuer verificationIssuer;
    private final CompositeMessageSender messageSender;
    private final DateTimeProvider dateTimeProvider;

    @Override
    public void issueSignupVerification(IssueVerificationCommand issueVerificationCommand) {
        IssuedVerification issued = verificationIssuer.issue(issueVerificationCommand.getTarget());

        verificationRepository.save(issued.verification());

        issued.message().ifPresent(message -> messageSender.sendVerification(issueVerificationCommand.getTarget(), message)
        );
    }

    @Override
    public void confirmSignupVerification(ConfirmVerificationCommand command) {
        Verification verification = verificationRepository.findByTarget(command.getTarget());

        boolean success = verification.verify(command.getCode(), dateTimeProvider.now());

        if (!success) {
            throw DomainRuleException.of(ErrorCode.AUTH_FAIL);
        }

        verificationRepository.save(verification);
    }

    @Override
    public void validateSignupVerification(VerificationTarget target) {
        Verification verification = verificationRepository.findByTarget(target);

        if (!verification.isVerified()) {
            throw DomainRuleException.of(ErrorCode.AUTH_FAIL);
        }
    }
}
