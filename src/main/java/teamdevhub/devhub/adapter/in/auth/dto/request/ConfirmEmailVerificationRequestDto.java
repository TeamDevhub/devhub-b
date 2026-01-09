package teamdevhub.devhub.adapter.in.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.common.enums.RegexPattern;
import teamdevhub.devhub.adapter.in.web.validator.RegexMatch;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmEmailVerificationRequestDto {

    @NotBlank
    @RegexMatch(RegexPattern.AUTH_EMAIL)
    private String email;

    @NotBlank
    @Size(min = 6, max = 6)
    private String code;
}