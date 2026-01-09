package teamdevhub.devhub.small.adapter.out.user.entity;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.domain.user.UserRole;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    void userEntity_생성_및_getter_테스트() {
        // given, when
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

        // then
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