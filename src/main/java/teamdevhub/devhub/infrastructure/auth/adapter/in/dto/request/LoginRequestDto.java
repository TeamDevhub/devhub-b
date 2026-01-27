package teamdevhub.devhub.infrastructure.auth.adapter.in.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.enums.RegexPattern;
import teamdevhub.devhub.shared.web.validator.RegexMatch;
import teamdevhub.devhub.core.auth.port.in.command.LoginCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "이메일은 필수입니다")
    @RegexMatch(RegexPattern.AUTH_EMAIL)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @RegexMatch(RegexPattern.AUTH_PASSWORD)
    private String password;

    public LoginCommand toCommand() {
        return LoginCommand.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
