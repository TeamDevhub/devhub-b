package teamdevhub.devhub.medium.mock.constant;

import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public final class TestConstant {

    private TestConstant() {}

    public static final String ADMIN_GUID = "ADMINa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String ADMIN_EMAIL = "admin@example.com";
    public static final String ADMIN_USERNAME = "AdminUser";
    public static final String ADMIN_PASSWORD = "adminPassword123";

    public static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    public static final String TEST_EMAIL = "user@example.com";
    public static final String TEST_PASSWORD = "password123!";
    public static final String TEST_USERNAME = "User";
    public static final String TEST_INTRO = "Hello World";
    public static final double TEST_MANNER = 36.5;
    public static final boolean TEST_BLOCKED = false;
    public static final boolean TEST_DELETED = false;
    public static final LocalDateTime TEST_LAST_LOGIN = LocalDateTime.of(2025, 1, 1, 12, 0);

    public static final String TEST_POSITION_GUID = "POSa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_POSITION_CD = "001";
    public static final String TEST_USER_SKILL_GUID = "SKLa1b2c3d4e5f6g7h8i9j10k11l12";
    public static final String TEST_SKILL_CD = "001";

    public static final List<String> TEST_POSITION_LIST = List.of("001");
    public static final List<String> TEST_SKILL_LIST = List.of("001");

    public static final Set<UserPosition> TEST_POSITIONS = Set.of(new UserPosition("001"));
    public static final Set<UserSkill> TEST_SKILLS = Set.of(new UserSkill("001"));

    public static final String EMAIL_CODE = "123456";
    public static final String UNVERIFIED_EMAIL = "unverified@example.com";

    public static final String NEW_USERNAME = "NewUsername";
    public static final String NEW_INTRO = "NewIntro";

    public static final List<String> NEW_POSITION_LIST = List.of("002");
    public static final List<String> NEW_SKILL_LIST = List.of("002");

    public static final Set<UserPosition> NEW_POSITIONS = Set.of(new UserPosition("002"));
    public static final Set<UserSkill> NEW_SKILLS = Set.of(new UserSkill("002"));
}
