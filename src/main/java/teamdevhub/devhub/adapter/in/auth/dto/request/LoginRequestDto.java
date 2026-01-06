package teamdevhub.devhub.adapter.in.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import teamdevhub.devhub.common.enums.RegexPatternEnum;
import teamdevhub.devhub.adapter.in.common.annotation.RegexMatch;

@Getter
public class LoginRequestDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @RegexMatch(RegexPatternEnum.AUTH_EMAIL)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @RegexMatch(RegexPatternEnum.AUTH_PASSWORD)
    private String password;
}
