package teamdevhub.devhub.constant;

import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public final class UserTestConstant {

    private UserTestConstant() {}

    public static final String ADMIN_USER_GUID = "ADMINa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String ADMIN_EMAIL = "admin@example.com";
    public static final String ADMIN_USERNAME = "AdminUser";
    public static final String ADMIN_PASSWORD = "adminPassword123";

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

    public static final String EMAIL_CODE = "123456";
    public static final String UNVERIFIED_EMAIL = "unverified@example.com";
    public static final String NEW_POSITION_CD = "002";
    public static final String NEW_SKILL_CD = "002";

    public static final String NEW_USERNAME = "NewUsername";
    public static final String NEW_INTRO = "NewIntro";

    public static final List<String> NEW_POSITION_LIST = List.of("002");
    public static final List<String> NEW_SKILL_LIST = List.of("002");

    public static final Set<UserPosition> NEW_USER_POSITIONS = Set.of(new UserPosition(TEST_USER_GUID_1,"002"));
    public static final Set<UserSkill> NEW_USER_SKILLS = Set.of(new UserSkill(TEST_USER_GUID_1,"002"));
}
