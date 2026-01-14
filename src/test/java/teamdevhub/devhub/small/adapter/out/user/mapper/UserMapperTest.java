package teamdevhub.devhub.small.adapter.out.user.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserMapperTest {

    @Test
    @DisplayName("UserEntity_를_AuthenticatedUser_로_변환할_수_있다")
    void convertEntityToAuthenticatedUser() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userGuid(TEST_USER_GUID_1)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .userRole(UserRole.USER)
                .build();

        // when
        AuthenticatedUser authenticatedUser = UserMapper.toAuthenticatedUser(userEntity);

        // then
        assertThat(authenticatedUser.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(authenticatedUser.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(authenticatedUser.password()).isEqualTo(TEST_PASSWORD_1);
        assertThat(authenticatedUser.userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("User_를_UserEntity_로_변환할_수_있다")
    void convertDomainToEntity() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        UserEntity userEntity = UserMapper.toEntity(user);

        // then
        assertThat(userEntity.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userEntity.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(userEntity.getPassword()).isEqualTo(TEST_PASSWORD_1);
        assertThat(userEntity.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(userEntity.getIntroduction()).isEqualTo(TEST_INTRO_1);
        assertThat(userEntity.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("UserEntity_를_User_로_변환할_수_있다")
    void convertEntityToDomain() {
        // given
        UserEntity entity = UserEntity.builder()
                .userGuid(TEST_USER_GUID_1)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .introduction(TEST_INTRO_1)
                .build();

        // when
        User user = UserMapper.toDomain(entity);

        // then
        assertThat(user.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME_1);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO_1);
    }
}