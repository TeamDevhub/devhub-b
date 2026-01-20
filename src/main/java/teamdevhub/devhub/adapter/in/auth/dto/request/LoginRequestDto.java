package teamdevhub.devhub.adapter.in.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.common.enums.RegexPattern;
import teamdevhub.devhub.adapter.in.web.validator.RegexMatch;
import teamdevhub.devhub.port.in.auth.command.LoginCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @RegexMatch(RegexPattern.AUTH_EMAIL)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @RegexMatch(RegexPattern.AUTH_PASSWORD)
    private String password;

    public LoginCommand toCommand() {
        return LoginCommand.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
