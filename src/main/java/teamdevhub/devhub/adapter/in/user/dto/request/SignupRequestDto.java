package teamdevhub.devhub.adapter.in.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import teamdevhub.devhub.common.enums.RegexPatternEnum;
import teamdevhub.devhub.common.validation.regex.RegexMatch;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @RegexMatch(RegexPatternEnum.AUTH_EMAIL)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @RegexMatch(RegexPatternEnum.AUTH_PASSWORD)
    private String password;

    @NotBlank(message = "사용자명은 필수입니다.")
    @RegexMatch(RegexPatternEnum.USERNAME)
    private String username;

    private String introduction;
}