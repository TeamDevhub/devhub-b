package teamdevhub.devhub.port.in.verification.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import teamdevhub.devhub.domain.verification.VerificationTarget;

@Getter
@AllArgsConstructor
public class IssueVerificationCommand {

    private final VerificationTarget target;
}
