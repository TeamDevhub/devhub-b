package teamdevhub.devhub.port.in.oauth.command;

import lombok.Builder;

import java.util.List;

@Builder
public record OauthSignupCommand(String tempToken, String password, String username,  String introduction,
                                 List<String> positionList, List<String> skillList) {

}
