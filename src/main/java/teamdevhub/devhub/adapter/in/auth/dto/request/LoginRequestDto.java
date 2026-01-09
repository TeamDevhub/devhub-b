package teamdevhub.devhub.adapter.in.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import teamdevhub.devhub.common.enums.RegexPattern;
import teamdevhub.devhub.adapter.in.common.validator.regex.RegexMatch;

@Getter
public class LoginRequestDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @RegexMatch(RegexPattern.AUTH_EMAIL)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @RegexMatch(RegexPattern.AUTH_PASSWORD)
    private String password;
}
