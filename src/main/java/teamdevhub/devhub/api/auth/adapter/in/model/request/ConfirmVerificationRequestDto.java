package teamdevhub.devhub.api.auth.adapter.in.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.core.auth.port.in.command.ConfirmVerificationCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmVerificationRequestDto {

    @NotNull
    private String verificationType;

    @NotBlank
    private String value;

    @NotBlank
    private String code;

    public ConfirmVerificationCommand toConfirmVerificationCommand() {
        VerificationType verificationType = VerificationType.from(this.verificationType);
        return new ConfirmVerificationCommand(VerificationTarget.of(verificationType, value), code);
    }
}
