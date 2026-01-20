package teamdevhub.devhub.adapter.in.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.port.in.oauth.command.OauthLoginCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OauthLoginRequestDto {

    @NotBlank(message = "필수값이 누락되었습니다.")
    private String tempToken;

    public OauthLoginCommand toCommand() {
        return OauthLoginCommand.builder()
                .tempToken(this.tempToken)
                .build();
    }
}
