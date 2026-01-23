package teamdevhub.devhub.domain.user.vo.user;

import lombok.Builder;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.command.SignupOauthUserCommand;
import teamdevhub.devhub.port.in.user.command.SignupAdminCommand;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;

import java.util.List;

@Builder
public record CreateUserCommand(
        String userGuid,
        VerificationProvider verificationProvider,
        String oauthId,
        String email,
        String encodedPassword,
        String username,
        String introduction,
        List<String> positionList,
        List<String> skillList
) {

    public static CreateUserCommand adminUserCreateCommand(SignupAdminCommand signupAdminCommand, String userGuid, String encodedPassword) {
        return new CreateUserCommand(
                userGuid,
                VerificationProvider.EMAIL,
                signupAdminCommand.email(),
                signupAdminCommand.email(),
                encodedPassword,
                signupAdminCommand.username(),
                signupAdminCommand.introduction(),
                signupAdminCommand.positionList(),
                signupAdminCommand.skillList()
        );
    }

    public static CreateUserCommand generalUserCreateCommand(SignupUserCommand signupUserCommand, String userGuid, String encodedPassword) {
        return new CreateUserCommand(
                userGuid,
                VerificationProvider.EMAIL,
                signupUserCommand.email(),
                signupUserCommand.email(),
                encodedPassword,
                signupUserCommand.username(),
                signupUserCommand.introduction(),
                signupUserCommand.positionList(),
                signupUserCommand.skillList()
        );
    }

    public static CreateUserCommand oauthUserCreateCommand(SignupOauthUserCommand signupOauthUserCommand, OauthUser oauthUser, String userGuid, String encodedPassword) {
        return new CreateUserCommand(
                userGuid,
                oauthUser.verificationProvider(),
                oauthUser.oauthId(),
                oauthUser.email(),
                encodedPassword,
                signupOauthUserCommand.username(),
                signupOauthUserCommand.introduction(),
                signupOauthUserCommand.positionList(),
                signupOauthUserCommand.skillList()
        );
    }
}
