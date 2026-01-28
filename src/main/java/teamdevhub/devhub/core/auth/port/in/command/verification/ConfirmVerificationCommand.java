package teamdevhub.devhub.core.auth.port.in.command.verification;

import lombok.Builder;
import teamdevhub.devhub.core.auth.domain.vo.VerificationTarget;

@Builder
public record ConfirmVerificationCommand(VerificationTarget verificationTarget, String code) {}
