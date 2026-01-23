package teamdevhub.devhub.small.adapter.out.user.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.port.in.user.command.SignupUserCommand;

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
        assertThat(authenticatedUser.userGuid()).isEqualTo(userEntity.getUserGuid());
        assertThat(authenticatedUser.email()).isEqualTo(userEntity.getEmail());
        assertThat(authenticatedUser.userRole()).isEqualTo(userEntity.getUserRole());
    }

    @Test
    @DisplayName("User_를_UserEntity_로_변환할_수_있다")
    void convertDomainToEntity() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        // when
        UserEntity userEntity = UserMapper.toEntity(testUser);

        // then
        assertThat(userEntity.getUserGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(userEntity.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(userEntity.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(userEntity.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("UserEntity_를_User_로_변환할_수_있다")
    void convertEntityToDomain() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userGuid(TEST_USER_GUID_1)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .introduction(TEST_INTRO_1)
                .build();

        // when
        User user = UserMapper.toDomain(userEntity);

        // then
        assertThat(user.getUserGuid()).isEqualTo(userEntity.getUserGuid());
        assertThat(user.getEmail()).isEqualTo(userEntity.getEmail());
    }
}