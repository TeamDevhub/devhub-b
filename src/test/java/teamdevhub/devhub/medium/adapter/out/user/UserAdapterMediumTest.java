package teamdevhub.devhub.medium.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.adapter.in.common.vo.PageResult;
import teamdevhub.devhub.adapter.out.user.UserAdapter;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.adapter.out.infrastructure.persistence.user.JpaUserRepository;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.domain.user.vo.user.CreateUserCommand;
import teamdevhub.devhub.domain.user.vo.user.UpdateUserCommand;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.user.command.AdminSignupCommand;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest
@Transactional
class UserAdapterMediumTest {

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
        AdminSignupCommand adminSignupCommand = AdminSignupCommand.builder()
                .userGuid(null)
                .email(ADMIN_EMAIL_1)
                .password(ADMIN_PASSWORD_1)
                .username(ADMIN_USERNAME_1)
                .introduction("")
                .positionList(List.of())
                .skillList(List.of())
                .verificationTarget(null)
                .build();
        CreateUserCommand adminUserCreateCommand = CreateUserCommand.adminUserCreateCommand(adminSignupCommand, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);
        User adminUser = User.createAdminUser(adminUserCreateCommand);

        // when
        userAdapter.saveAdminUser(adminUser);

        // then
        UserEntity saved = jpaUserRepository.findByUserGuid(adminUser.getUserGuid()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(adminUser.getEmail());
    }

    @Test
    @DisplayName("로그인을_시도하면_ID_값인_이메일로_AuthenticatedUser_를_조회한다")
    void getAuthenticatedUserByLoginId() {
        // given
        SignupCommand signupCommand = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

        jpaUserRepository.save(UserMapper.toEntity(testUser));

        // when
        AuthenticatedUser authenticatedUser = userAdapter.findAuthenticatedUserByEmail(testUser.getEmail());

        // then
        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.email()).isEqualTo(testUser.getEmail());
    }

    @Test
    @DisplayName("새로운_사용자를_생성하면_사용자_정보를_저장한다")
    void saveUser() {
        // given
        SignupCommand signupCommand = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

        // when
        User savedUser = userAdapter.save(testUser);

        // then
        assertThat(savedUser.getUserGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(savedUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedUser.getUsername()).isEqualTo(testUser.getUsername());
    }

    @Test
    @DisplayName("사용자_식별키로_User_를_조회한다")
    void getUserByIdentifier() {
        // given
        SignupCommand signupCommand = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

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
        SignupCommand signupCommand = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

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
        SignupCommand signupCommand = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalUserCreateCommand = CreateUserCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

        jpaUserRepository.save(UserMapper.toEntity(testUser));

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
        AdminSignupCommand adminSignupCommand = AdminSignupCommand.builder()
                .userGuid(null)
                .email(ADMIN_EMAIL_1)
                .password(ADMIN_PASSWORD_1)
                .username(ADMIN_USERNAME_1)
                .introduction("")
                .positionList(List.of())
                .skillList(List.of())
                .verificationTarget(null)
                .build();
        CreateUserCommand adminUserCreateCommand = CreateUserCommand.adminUserCreateCommand(adminSignupCommand, ADMIN_USER_GUID_1, ADMIN_PASSWORD_1);
        User adminUser = User.createAdminUser(adminUserCreateCommand);

        jpaUserRepository.save(UserMapper.toEntity(adminUser));

        // when
        boolean exists = userAdapter.existsByUserRole(UserRole.ADMIN);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("사용자_목록_조회를_하면_AdminUserSummaryResponseDto_로_된_PageResult_데이터를_반환한다")
    void getUserListAsAdminSummary() {
        // given
        SignupCommand signupCommand1 = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalUserCreateCommand1 = CreateUserCommand.generalUserCreateCommand(signupCommand1, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser1 = User.createGeneralUser(generalUserCreateCommand1);

        SignupCommand signupCommand2 = SignupCommand.builder()
                .userGuid(null)
                .email(TEST_EMAIL_2)
                .password(TEST_PASSWORD_2)
                .username(TEST_USERNAME_2)
                .introduction(TEST_INTRO_2)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_2)
                .build();
        CreateUserCommand generalUserCreateCommand2 = CreateUserCommand.generalUserCreateCommand(signupCommand2, TEST_USER_GUID_2, TEST_PASSWORD_2);
        User testUser2 = User.createGeneralUser(generalUserCreateCommand2);

        jpaUserRepository.save(UserMapper.toEntity(testUser1));
        jpaUserRepository.save(UserMapper.toEntity(testUser2));

        SearchUserCommand searchCommand = new SearchUserCommand(null, null, null, null);

        // when
        PageResult<User> page = userAdapter.listUser(searchCommand, 0, 10);

        // then
        assertThat(page.content()).hasSize(2);
        assertThat(page.content().get(0).getEmail()).isEqualTo(testUser2.getEmail());
        assertThat(page.content().get(1).getEmail()).isEqualTo(testUser1.getEmail());
    }
}