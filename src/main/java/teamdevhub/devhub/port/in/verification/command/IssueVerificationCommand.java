package teamdevhub.devhub.port.in.verification.command;

import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

public record IssueVerificationCommand(VerificationTarget verificationTarget) { }
