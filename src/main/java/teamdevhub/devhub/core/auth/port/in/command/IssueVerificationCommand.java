package teamdevhub.devhub.core.auth.port.in.command;

import lombok.Builder;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;

@Builder
public record IssueVerificationCommand(VerificationTarget verificationTarget) { }
