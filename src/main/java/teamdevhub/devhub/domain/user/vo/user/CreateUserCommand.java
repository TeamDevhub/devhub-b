package teamdevhub.devhub.domain.user.vo.user;

import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.port.in.user.command.AdminSignupCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import java.util.List;

public record CreateUserCommand(
        String userGuid,
        String email,
        String encodedPassword,
        String username,
        String introduction,
        List<String> positionList,
        List<String> skillList,
        VerificationTarget verificationTarget
) {

    public static CreateUserCommand adminUserCreateCommand(AdminSignupCommand adminSignupCommand, String userGuid, String encodedPassword) {
        return new CreateUserCommand(
                userGuid,
                adminSignupCommand.getEmail(),
                encodedPassword,
                adminSignupCommand.getUsername(),
                adminSignupCommand.getIntroduction(),
                adminSignupCommand.getPositionList(),
                adminSignupCommand.getSkillList(),
                adminSignupCommand.getVerificationTarget()
        );
    }

    public static CreateUserCommand generalUserCreateCommand(SignupCommand signupCommand, String userGuid, String encodedPassword) {
        return new CreateUserCommand(
                userGuid,
                signupCommand.email(),
                encodedPassword,
                signupCommand.username(),
                signupCommand.introduction(),
                signupCommand.positionList(),
                signupCommand.skillList(),
                signupCommand.verificationTarget()
        );
    }

//    public static CreateUserCommand oauthUserCreateCommand(OauthSignupCommand oauthSignupCommand, String userGuid, String encodedPassword) {
//        return new CreateUserCommand(
//                userGuid,
//                oauthSignupCommand.getEmail(),
//                encodedPassword,
//                oauthSignupCommand.getUsername(),
//                oauthSignupCommand.getIntroduction(),
//                oauthSignupCommand.getPositionList(),
//                oauthSignupCommand.getSkillList()
//        );
//    }
}
