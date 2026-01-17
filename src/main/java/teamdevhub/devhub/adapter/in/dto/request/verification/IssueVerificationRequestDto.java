package teamdevhub.devhub.adapter.in.dto.request.verification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.port.in.verification.command.IssueVerificationCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueVerificationRequestDto {

    @NotNull
    private VerificationType verificationType;

    @NotBlank
    private String value;

    public IssueVerificationCommand toIssueVerificationCommand() {
        return new IssueVerificationCommand(VerificationTarget.of(verificationType, value));
    }
}
