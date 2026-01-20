package teamdevhub.devhub.port.in.oauth.command;

import lombok.Builder;

import java.util.List;

@Builder
public record OauthSignupCommand(String tempToken, String username, String password, String introduction,
                                 List<String> positionList, List<String> skillList) {

}
