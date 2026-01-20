package teamdevhub.devhub.constant;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.position.UserPosition;
import teamdevhub.devhub.domain.user.vo.skill.UserSkill;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.domain.verification.vo.VerificationTarget;
import teamdevhub.devhub.domain.verification.vo.VerificationType;
import teamdevhub.devhub.port.in.user.command.AdminSignupCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public final class UserTestConstant {

    private UserTestConstant() {}

    public static final String ADMIN_USER_GUID_1 = "1ADMNa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String ADMIN_EMAIL_1 = "admin1@example.com";
    public static final String ADMIN_USERNAME_1 = "AdminUser1";
    public static final String ADMIN_PASSWORD_1 = "adminPassword123!";

    public static final String ADMIN_USER_GUID_2 = "2ADMNa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String ADMIN_EMAIL_2 = "admin2@example.com";
    public static final String ADMIN_USERNAME_2 = "AdminUser2";
    public static final String ADMIN_PASSWORD_2 = "adminPassword456!";

    public static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_EMAIL_1 = "user1@example.com";
    public static final String TEST_PASSWORD_1 = "password123!";
    public static final String TEST_USERNAME_1 = "User1";
    public static final String TEST_INTRO_1 = "Hello World";

    public static final String TEST_USER_GUID_2 = "USER2a1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_EMAIL_2 = "user2@example.com";
    public static final String TEST_PASSWORD_2 = "password456!";
    public static final String TEST_USERNAME_2 = "User2";
    public static final String TEST_INTRO_2 = "Goodbye World";

    public static final double TEST_MANNER = 36.5;
    public static final boolean TEST_BLOCKED = false;
    public static final boolean TEST_DELETED = false;
    public static final LocalDateTime TEST_LAST_LOGIN = LocalDateTime.of(2025, 1, 1, 12, 0);

    public static final String TEST_USER_POSITION_GUID = "POSa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_USER_SKILL_GUID = "SKLa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_POSITION_CD = "001";
    public static final String TEST_SKILL_CD = "001";

    public static final List<String> TEST_POSITION_LIST = List.of("001");
    public static final List<String> TEST_SKILL_LIST = List.of("001");

    public static final Set<UserPosition> TEST_USER_POSITIONS = Set.of(new UserPosition(TEST_USER_GUID_1,"001"));
    public static final Set<UserSkill> TEST_USER_SKILLS = Set.of(new UserSkill(TEST_USER_GUID_1, "001"));

    public static final String TEST_EMAIL_CODE = "123456";
    public static final String TEST_EMAIL = "test@test.com";
    public static final String UNVERIFIED_EMAIL = "unverified@example.com";

    public static final String NEW_POSITION_CD = "002";
    public static final String NEW_SKILL_CD = "002";

    public static final String NEW_USERNAME = "NewUsername";
    public static final String NEW_INTRO = "NewIntro";

    public static final List<String> NEW_POSITION_LIST = List.of("002");
    public static final List<String> NEW_SKILL_LIST = List.of("002");

    public static final Set<UserPosition> NEW_USER_POSITIONS = Set.of(new UserPosition(TEST_USER_GUID_1,"002"));
    public static final Set<UserSkill> NEW_USER_SKILLS = Set.of(new UserSkill(TEST_USER_GUID_1,"002"));

    public static final VerificationTarget VERIFICATION_TARGET_1 = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);
    public static final VerificationTarget VERIFICATION_TARGET_2 = VerificationTarget.of(VerificationType.EMAIL, UNVERIFIED_EMAIL);

//    public static final AdminSignupCommand ADMIN_SIGNUP_COMMAND_1 = AdminSignupCommand.builder()
//            .userGuid(null)
//            .email(ADMIN_EMAIL_1)
//            .password(ADMIN_PASSWORD_1)
//            .username(ADMIN_USERNAME_1)
//            .introduction("")
//            .positionList(List.of())
//            .skillList(List.of())
//            .verificationTarget(null)
//            .build();
//
//    public static final CreateUserCommand CREATE_USER_COMMAND_ADMIN_1 = CreateUserCommand.adminUserCreateCommand(ADMIN_SIGNUP_COMMAND_1, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);
//
//    public static final User ADMIN_USER_1 = User.createAdminUser(CREATE_USER_COMMAND_ADMIN_1);
//
//    public static final AdminSignupCommand ADMIN_SIGNUP_COMMAND_2 = AdminSignupCommand.builder()
//            .userGuid(null)
//            .email(ADMIN_EMAIL_2)
//            .password(ADMIN_PASSWORD_2)
//            .username(ADMIN_USERNAME_2)
//            .introduction("")
//            .positionList(List.of())
//            .skillList(List.of())
//            .verificationTarget(null)
//            .build();
//
//    public static final CreateUserCommand CREATE_USER_COMMAND_ADMIN_2 = CreateUserCommand.adminUserCreateCommand(ADMIN_SIGNUP_COMMAND_2, ADMIN_USER_GUID_2, ADMIN_PASSWORD_2);
//
//    public static final User ADMIN_USER_2 = User.createAdminUser(CREATE_USER_COMMAND_ADMIN_2);
//
//
//    public static final SignupCommand SIGNUP_COMMAND_1 = SignupCommand.builder()
//            .userGuid(null)
//            .email(TEST_EMAIL_1)
//            .password(TEST_PASSWORD_1)
//            .username(TEST_USERNAME_1)
//            .introduction(TEST_INTRO_1)
//            .positionList(TEST_POSITION_LIST)
//            .skillList(TEST_SKILL_LIST)
//            .verificationTarget(VERIFICATION_TARGET_1)
//            .build();
//
//    public static final CreateUserCommand CREATE_USER_COMMAND_GENERAL_1 = CreateUserCommand.generalUserCreateCommand(SIGNUP_COMMAND_1, TEST_USER_GUID_1, TEST_PASSWORD_1);
//
//    public static final User TEST_USER_1 = User.createGeneralUser(CREATE_USER_COMMAND_GENERAL_1);
//
//    public static final SignupCommand SIGNUP_COMMAND_2 = SignupCommand.builder()
//            .userGuid(null)
//            .email(TEST_EMAIL_2)
//            .password(TEST_PASSWORD_2)
//            .username(TEST_USERNAME_2)
//            .introduction(TEST_INTRO_2)
//            .positionList(TEST_POSITION_LIST)
//            .skillList(TEST_SKILL_LIST)
//            .verificationTarget(VERIFICATION_TARGET_2)
//            .build();
//
//    public static final CreateUserCommand CREATE_USER_COMMAND_GENERAL_2 = CreateUserCommand.generalUserCreateCommand(SIGNUP_COMMAND_2, TEST_USER_GUID_2, TEST_PASSWORD_2);
//
//    public static final User TEST_USER_2 = User.createGeneralUser(CREATE_USER_COMMAND_GENERAL_2);
}
