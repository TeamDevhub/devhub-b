package teamdevhub.devhub.small.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.page.PageResult;
import teamdevhub.devhub.adapter.out.user.UserAdapter;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.user.CreateUserCommand;
import teamdevhub.devhub.core.user.domain.vo.user.UpdateUserCommand;
import teamdevhub.devhub.fake.framework.persistence.user.FakeJpaUserRepository;
import teamdevhub.devhub.fake.framework.persistence.user.FakeUserQueryRepository;
import teamdevhub.devhub.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.core.common.page.PageCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserAdapterTest {

    private UserAdapter userAdapter;

    private FakeJpaUserRepository fakeJpaUserRepository;

    @BeforeEach
    void init() {
        fakeJpaUserRepository = new FakeJpaUserRepository();
        FakeUserQueryRepository fakeUserQueryRepository = new FakeUserQueryRepository();

        userAdapter = new UserAdapter(fakeJpaUserRepository, fakeUserQueryRepository);
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
        UserEntity saved = fakeJpaUserRepository.findByUserGuid(adminUser.getUserGuid()).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(adminUser.getEmail());
    }

    @Test
    @DisplayName("로그인을_시도하면_ID_값인_이메일로_AuthenticatedUser_를_조회한다")
    void getAuthenticatedUserByLoginId() {
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

        fakeJpaUserRepository.saveForSignup(UserMapper.toEntity(adminUser));

        // when
        AuthenticatedUser authenticatedUser = userAdapter.findAuthenticatedUserByEmail(adminUser.getEmail());

        // then
        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.userGuid()).isEqualTo(adminUser.getUserGuid());
        assertThat(authenticatedUser.email()).isEqualTo(adminUser.getEmail());
        assertThat(authenticatedUser.userRole()).isEqualTo(adminUser.getUserRole());
    }

    @Test
    @DisplayName("새로운_사용자를_생성하면_사용자_정보를_저장한다")
    void saveUserWithPositionsAndSkills() {
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
        User savedUser = userAdapter.save(testUser);

        // then
        assertThat(savedUser.getUserGuid()).isEqualTo(testUser.getUserGuid());
        assertThat(savedUser.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(savedUser.getUserRole()).isEqualTo(testUser.getUserRole());
    }

    @Test
    @DisplayName("사용자_식별키로_User_를_조회한다")
    void getUserByIdentifier() {
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

        fakeJpaUserRepository.save(UserMapper.toEntity(testUser));

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

        UpdateUserCommand updateUserCommand = new UpdateUserCommand(NEW_USERNAME, NEW_INTRO);
        testUser.updateBasicProfile(updateUserCommand);

        // when
        userAdapter.updateUserProfile(testUser);

        // then
        User updatedUser = fakeJpaUserRepository.findByUserGuid(testUser.getUserGuid())
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

        fakeJpaUserRepository.save(UserMapper.toEntity(testUser));
        testUser.withdraw();

        // when
        userAdapter.delete(testUser);

        // then
        assertThat(fakeJpaUserRepository.findByUserGuid(testUser.getUserGuid())
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

        fakeJpaUserRepository.save(UserMapper.toEntity(adminUser));

        // when
        boolean exists = userAdapter.existsByUserRole(adminUser.getUserRole());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("사용자_목록_조회를_하면_AdminUserSummaryResponseDto_로_된_PageResult_데이터를_반환한다")
    void getUserListAsAdminSummary() {
        // given
        PageCommand pageCommand = new PageCommand(0, 10);
        SearchUserCommand searchCommand = new SearchUserCommand(null, null, null, null);

        // when
        PageResult<User> pagedUserList = userAdapter.listUser(searchCommand, pageCommand.page(), pageCommand.size());

        // then
        assertThat(pagedUserList.content()).hasSize(2);
        assertThat(pagedUserList.content().get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(pagedUserList.content().get(1).getEmail()).isEqualTo("user2@example.com");
    }
}