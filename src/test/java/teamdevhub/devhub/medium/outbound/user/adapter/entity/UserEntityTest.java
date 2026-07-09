package teamdevhub.devhub.medium.outbound.user.adapter.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserEntityTest {

    @Test
    @DisplayName("userEntity_생성_및_getter_를_사용할_수_있다")
    void createEntityAndUseGetter() {
        // given, when
        UserEntity userEntity = UserEntity.builder()
                .userGuid(TEST_USER_GUID_1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .introduction(TEST_INTRO_1)
                .mannerDegree(TEST_MANNER)
                .blocked(TEST_BLOCKED)
                .deleted(TEST_DELETED)
                .build();

        // then
        assertThat(userEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userEntity.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(userEntity.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(userEntity.getIntroduction()).isEqualTo(TEST_INTRO_1);
        assertThat(userEntity.getMannerDegree()).isEqualTo(TEST_MANNER);
        assertThat(userEntity.isBlocked()).isEqualTo(TEST_BLOCKED);
        assertThat(userEntity.isDeleted()).isEqualTo(TEST_DELETED);
    }
}