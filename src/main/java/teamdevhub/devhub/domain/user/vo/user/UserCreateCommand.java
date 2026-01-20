package teamdevhub.devhub.domain.user.vo.user;

import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.auth.vo.user.OauthUser;
import teamdevhub.devhub.port.in.oauth.command.OauthSignupCommand;
import teamdevhub.devhub.port.in.user.command.AdminSignupCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import java.util.List;

public record UserCreateCommand(
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

    public static UserCreateCommand adminUserCreateCommand(AdminSignupCommand adminSignupCommand, String userGuid, String encodedPassword) {
        return new UserCreateCommand(
                userGuid,
                VerificationProvider.EMAIL,
                adminSignupCommand.email(),
                adminSignupCommand.email(),
                encodedPassword,
                adminSignupCommand.username(),
                adminSignupCommand.introduction(),
                adminSignupCommand.positionList(),
                adminSignupCommand.skillList()
        );
    }

    public static UserCreateCommand generalUserCreateCommand(SignupCommand signupCommand, String userGuid, String encodedPassword) {
        return new UserCreateCommand(
                userGuid,
                VerificationProvider.EMAIL,
                signupCommand.email(),
                signupCommand.email(),
                encodedPassword,
                signupCommand.username(),
                signupCommand.introduction(),
                signupCommand.positionList(),
                signupCommand.skillList()
        );
    }

    public static UserCreateCommand oauthUserCreateCommand(OauthSignupCommand oauthSignupCommand, OauthUser oauthUser, String userGuid, String encodedPassword) {
        return new UserCreateCommand(
                userGuid,
                oauthUser.verificationProvider(),
                oauthUser.oauthId(),
                oauthUser.email(),
                encodedPassword,
                oauthSignupCommand.username(),
                oauthSignupCommand.introduction(),
                oauthSignupCommand.positionList(),
                oauthSignupCommand.skillList()
        );
    }
}
