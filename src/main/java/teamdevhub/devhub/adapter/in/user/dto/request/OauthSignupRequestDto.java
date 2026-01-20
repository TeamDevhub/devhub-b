package teamdevhub.devhub.adapter.in.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.adapter.in.web.validator.RegexMatch;
import teamdevhub.devhub.common.enums.RegexPattern;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OauthSignupRequestDto {

    @NotBlank(message = "TEMP 토큰은 필수입니다.")
    private String tempToken;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @RegexMatch(RegexPattern.AUTH_PASSWORD)
    private String password;

    @RegexMatch(RegexPattern.USERNAME)
    private String username;

    private String introduction;

    @NotNull(message = "관심 포지션은 필수입니다.")
    @Size(min = 1, message = "관심 포지션은 최소 1개 이상 선택해야 합니다.")
    private List<@NotBlank String> positionList;

    @NotNull(message = "보유 스킬은 필수입니다.")
    @Size(min = 1, message = "보유 스킬은 최소 1개 이상 선택해야 합니다.")
    private List<@NotBlank String> skillList;

    public OauthSignupCommand toCommand() {
        return OauthSignupCommand.builder()
                .tempToken(this.tempToken)
                .password(this.password)
                .username(this.username)
                .introduction(this.introduction)
                .positionList(this.positionList)
                .skillList(this.skillList)
                .build();
    }
}
