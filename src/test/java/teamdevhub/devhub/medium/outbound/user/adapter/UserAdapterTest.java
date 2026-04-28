package teamdevhub.devhub.medium.outbound.user.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.domain.vo.command.UpdateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.outbound.user.adapter.UserAdapter;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.outbound.user.adapter.mapper.UserMapper;
import teamdevhub.devhub.outbound.user.persistence.JpaUserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest
@Transactional
class UserAdapterTest {

    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void init() {
        jpaUserRepository.deleteAll();
    }

    @Test
    @DisplayName("관리자_계정을_저장한다")
    void saveAdminAccount() {
        // given
        SignupAdminCommand signupAdminCommand = SignupAdminCommand.builder()
                .userGuid(null)
                .email(ADMIN_EMAIL_1)
                .password(ADMIN_PASSWORD_1)
                .username(ADMIN_USERNAME_1)
                .introduction("")
                .positionList(List.of())
                .skillList(List.of())
                .verificationTarget(null)
                .build();
        CreateUserCommand adminCreateUserCommand = CreateUserCommand.adminUserCreateCommand(signupAdminCommand, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);
        User adminUser = User.createAdminUser(adminCreateUserCommand);

        // when
        userAdapter.saveAdminUser(adminUser);

        // then
        UserEntity saved = jpaUserRepository.findByUserGuid(adminUser.getUserGuid()).orElse(null);
        assertThat(saved).isNotNull();
    }

    @Test
    @DisplayName("새로운_사용자를_생성하면_사용자_정보를_저장한다")
    void saveUser() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        // when
        User savedUser = userAdapter.save(testUser);

        // then
        assertThat(savedUser.getUserGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(savedUser.getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    @DisplayName("사용자_식별키로_User_를_조회한다")
    void getUserByIdentifier() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        jpaUserRepository.save(UserMapper.toEntity(testUser));

        // when
        User foundUser = userAdapter.findByUserGuid(testUser.getUserGuid());

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserGuid()).isEqualTo(testUser.getUserGuid());
    }

    @Test
    @DisplayName("사용자_프로필_정보를_수정하면_변경된_값이_저장된다")
    void updateUserProfile() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        jpaUserRepository.save(UserMapper.toEntity(testUser));
        UpdateUserCommand updateUserCommand = new UpdateUserCommand(NEW_USERNAME, NEW_INTRO);
        testUser.updateBasicProfile(updateUserCommand);

        // when
        userAdapter.updateUserProfile(testUser);

        // then
        User updatedUser = jpaUserRepository.findByUserGuid(testUser.getUserGuid())
                .map(UserMapper::toDomain)
                .orElseThrow();

        assertThat(updatedUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(updatedUser.getIntroduction()).isEqualTo(testUser.getIntroduction());
    }

    @Test
    @DisplayName("회원탈퇴한_사용자의_deleted_값은_true_이다")
    void isDeletedUser() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);


        testUser.withdraw();

        // when
        userAdapter.delete(testUser);

        // then
        assertThat(jpaUserRepository.findByUserGuid(testUser.getUserGuid())
                .orElseThrow()
                .isDeleted())
                .isTrue();
    }

    @Test
    @DisplayName("사용자_권한이_일치한다면_true_를_반환한다")
    void isUserRoleMatched() {
        // given
        SignupAdminCommand signupAdminCommand = SignupAdminCommand.builder()
                .userGuid(null)
                .email(ADMIN_EMAIL_1)
                .password(ADMIN_PASSWORD_1)
                .username(ADMIN_USERNAME_1)
                .introduction("")
                .positionList(List.of())
                .skillList(List.of())
                .verificationTarget(null)
                .build();
        CreateUserCommand adminCreateUserCommand = CreateUserCommand.adminUserCreateCommand(signupAdminCommand, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);
        User adminUser = User.createAdminUser(adminCreateUserCommand);

        jpaUserRepository.save(UserMapper.toEntity(adminUser));

        // when
        boolean exists = userAdapter.existsByUserRole(UserRole.ADMIN);

        // then
        assertThat(exists).isTrue();
    }
}