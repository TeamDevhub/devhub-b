package teamdevhub.devhub.adapter.in.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.common.enums.RegexPatternEnum;
import teamdevhub.devhub.adapter.in.common.annotation.RegexMatch;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmEmailCertificationRequestDto {

    @NotBlank
    @RegexMatch(RegexPatternEnum.AUTH_EMAIL)
    private String email;

    @NotBlank
    @Size(min = 6, max = 6)
    private String code;
}