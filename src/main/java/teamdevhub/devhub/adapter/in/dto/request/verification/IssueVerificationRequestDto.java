package teamdevhub.devhub.adapter.in.dto.request.verification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.domain.verification.VerificationType;

@Getter
@NoArgsConstructor
public class IssueVerificationRequestDto {

    @NotNull
    private VerificationType verificationType;

    @NotBlank
    private String value;
}
