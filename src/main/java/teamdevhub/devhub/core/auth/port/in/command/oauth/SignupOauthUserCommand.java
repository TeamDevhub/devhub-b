package teamdevhub.devhub.core.auth.port.in.command.oauth;

import lombok.Builder;

import java.util.List;

@Builder
public record SignupOauthUserCommand(String tempToken, String password, String username, String introduction,
                                     List<String> positionList, List<String> skillList) {

}
