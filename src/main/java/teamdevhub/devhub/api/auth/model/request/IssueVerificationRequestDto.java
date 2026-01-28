package teamdevhub.devhub.api.auth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.core.auth.port.in.command.verification.IssueVerificationCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueVerificationRequestDto {

    @NotNull
    private String verificationType;

    @NotBlank
    private String value;

    public IssueVerificationCommand toIssueVerificationCommand() {
        VerificationType verificationType = VerificationType.from(this.verificationType);
        return new IssueVerificationCommand(VerificationTarget.of(verificationType, value));
    }
}
