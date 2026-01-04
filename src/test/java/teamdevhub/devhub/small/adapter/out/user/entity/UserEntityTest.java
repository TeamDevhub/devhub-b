package teamdevhub.devhub.small.adapter.out.user.entity;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    private static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USERNAME = "User";
    private static final String TEST_INTRO = "Hello World";

    private static final double TEST_MANNER = 36.5;
    private static final boolean TEST_BLOCKED = false;
    private static final boolean TEST_DELETED = false;
    private static final LocalDateTime TEST_LAST_LOGIN = LocalDateTime.of(2025, 1, 1, 12, 0);

    @Test
    void userEntity_생성_및_getter_테스트() {
        //given, when
        UserEntity userEntity = UserEntity.builder()
                .userGuid(TEST_GUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .username(TEST_USERNAME)
                .userRole(UserRole.USER)
                .introduction(TEST_INTRO)
                .mannerDegree(TEST_MANNER)
                .blocked(TEST_BLOCKED)
                .deleted(TEST_DELETED)
                .lastLoginDt(TEST_LAST_LOGIN)
                .build();

        //then
        assertThat(userEntity.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(userEntity.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(userEntity.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(userEntity.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(userEntity.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(userEntity.getIntroduction()).isEqualTo(TEST_INTRO);
        assertThat(userEntity.getMannerDegree()).isEqualTo(TEST_MANNER);
        assertThat(userEntity.isBlocked()).isEqualTo(TEST_BLOCKED);
        assertThat(userEntity.isDeleted()).isEqualTo(TEST_DELETED);
        assertThat(userEntity.getLastLoginDt()).isEqualTo(TEST_LAST_LOGIN);
    }
}