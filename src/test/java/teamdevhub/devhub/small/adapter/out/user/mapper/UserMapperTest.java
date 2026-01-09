package teamdevhub.devhub.small.adapter.out.user.mapper;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.common.mock.constant.TestConstant.*;

class UserMapperTest {

    @Test
    void UserEntity_를_AuthenticatedUser_로_변환할_수_있다() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userGuid(TEST_GUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .userRole(UserRole.USER)
                .build();

        // when
        AuthenticatedUser authenticatedUser = UserMapper.toAuthenticatedUser(userEntity);

        // then
        assertThat(authenticatedUser.userGuid()).isEqualTo(TEST_GUID);
        assertThat(authenticatedUser.email()).isEqualTo(TEST_EMAIL);
        assertThat(authenticatedUser.password()).isEqualTo(TEST_PASSWORD);
        assertThat(authenticatedUser.userRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void User_를_UserEntity_로_변환할_수_있다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        UserEntity userEntity = UserMapper.toEntity(user);

        // then
        assertThat(userEntity.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(userEntity.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(userEntity.getPassword()).isEqualTo(TEST_PASSWORD);
        assertThat(userEntity.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(userEntity.getIntroduction()).isEqualTo(TEST_INTRO);
        assertThat(userEntity.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void UserEntity_를_User_로_변환할_수_있다() {
        // given
        UserEntity entity = UserEntity.builder()
                .userGuid(TEST_GUID)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .username(TEST_USERNAME)
                .userRole(UserRole.USER)
                .introduction(TEST_INTRO)
                .build();

        // when
        User user = UserMapper.toDomain(entity, TEST_POSITIONS, TEST_SKILLS);

        // then
        assertThat(user.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(user.getEmail()).isEqualTo(TEST_EMAIL);
        assertThat(user.getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(user.getIntroduction()).isEqualTo(TEST_INTRO);
        assertThat(user.getPositions()).contains(new UserPosition("001"));
        assertThat(user.getSkills()).contains(new UserSkill("001"));
    }
}