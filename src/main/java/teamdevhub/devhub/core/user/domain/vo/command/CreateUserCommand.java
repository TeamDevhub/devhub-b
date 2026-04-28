package teamdevhub.devhub.core.user.domain.vo.command;

import lombok.Builder;
import teamdevhub.devhub.core.auth.domain.vo.oauth.OauthUser;
import teamdevhub.devhub.core.auth.port.in.command.oauth.SignupOauthUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import java.util.List;

@Builder
public record CreateUserCommand(
        String userGuid,
        String username,
        String introduction,
        List<String> positionList,
        List<String> skillList
) {

    public static CreateUserCommand adminUserCreateCommand(SignupAdminCommand signupAdminCommand, String userGuid, String encodedPassword) {
        return new CreateUserCommand(
                userGuid,
                signupAdminCommand.username(),
                signupAdminCommand.introduction(),
                signupAdminCommand.positionList(),
                signupAdminCommand.skillList()
        );
    }

    public static CreateUserCommand generalUserCreateCommand(SignupUserCommand signupUserCommand, String userGuid) {
        return new CreateUserCommand(
                userGuid,
                signupUserCommand.username(),
                signupUserCommand.introduction(),
                signupUserCommand.positionList(),
                signupUserCommand.skillList()
        );
    }

    public static CreateUserCommand oauthUserCreateCommand(SignupOauthUserCommand signupOauthUserCommand, OauthUser oauthUser, String userGuid) {
        return new CreateUserCommand(
                userGuid,
                signupOauthUserCommand.username(),
                signupOauthUserCommand.introduction(),
                signupOauthUserCommand.positionList(),
                signupOauthUserCommand.skillList()
        );
    }
}
