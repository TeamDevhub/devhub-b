package teamdevhub.devhub.adapter.in.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.common.enums.RegexPattern;
import teamdevhub.devhub.adapter.in.common.validator.regex.RegexMatch;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @RegexMatch(RegexPattern.AUTH_EMAIL)
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @RegexMatch(RegexPattern.AUTH_PASSWORD)
    private String password;

    @NotBlank(message = "사용자명은 필수입니다.")
    @RegexMatch(RegexPattern.USERNAME)
    private String username;

    private String introduction;

    @NotNull(message = "관심 포지션은 필수입니다.")
    @Size(min = 1, message = "관심 포지션은 최소 1개 이상 선택해야 합니다.")
    private List<@NotBlank String> positionList;

    @NotNull(message = "보유 스킬은 필수입니다.")
    @Size(min = 1, message = "보유 스킬은 최소 1개 이상 선택해야 합니다.")
    private List<@NotBlank String> skillList;
}