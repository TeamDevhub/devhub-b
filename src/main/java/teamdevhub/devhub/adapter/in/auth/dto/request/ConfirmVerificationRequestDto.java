package teamdevhub.devhub.adapter.in.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.port.in.verification.command.ConfirmVerificationCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmVerificationRequestDto {

    @NotNull
    private VerificationType verificationType;

    @NotBlank
    private String value;

    @NotBlank
    private String code;

    public ConfirmVerificationCommand toConfirmVerificationCommand() {
        return new ConfirmVerificationCommand(VerificationTarget.of(verificationType, value), code);
    }
}
