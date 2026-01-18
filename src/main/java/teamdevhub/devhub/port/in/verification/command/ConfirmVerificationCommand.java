package teamdevhub.devhub.port.in.verification.command;

import lombok.Builder;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;

@Builder
public record ConfirmVerificationCommand(VerificationTarget verificationTarget, String code) {}
