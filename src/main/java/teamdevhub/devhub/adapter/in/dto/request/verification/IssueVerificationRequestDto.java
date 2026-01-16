package teamdevhub.devhub.adapter.in.dto.request.verification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.domain.verification.VerificationTarget;
import teamdevhub.devhub.domain.verification.VerificationType;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;

@Getter
@NoArgsConstructor
public class IssueVerificationRequestDto {

    @NotNull
    private VerificationType verificationType;

    @NotBlank
    private String value;

    public IssueVerificationCommand toIssueVerificationCommand() {
        return new IssueVerificationCommand(
                VerificationTarget.of(verificationType, value)
        );
    }
}
