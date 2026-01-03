package teamdevhub.devhub.adapter.in.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.common.enums.RegexPatternEnum;
import teamdevhub.devhub.common.validation.regex.RegexMatch;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailCertificationRequestDto {

    @NotBlank
    @RegexMatch(RegexPatternEnum.AUTH_EMAIL)
    private String email;
}