package teamdevhub.devhub.small.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.adapter.out.user.UserAdapter;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;
import teamdevhub.devhub.fake.spring.persistence.user.FakeJpaUserRepository;
import teamdevhub.devhub.fake.spring.persistence.user.FakeUserQueryRepository;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.common.command.PageCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserAdapterTest {

    private UserAdapter userAdapter;
    private FakeJpaUserRepository fakeJpaUserRepository;

    @BeforeEach
    void init() {
        fakeJpaUserRepository = new FakeJpaUserRepository();
        FakeUserQueryRepository fakeUserQueryRepository = new FakeUserQueryRepository();

        userAdapter = new UserAdapter(
                fakeJpaUserRepository,
                fakeUserQueryRepository
        );
    }

    @Test
    @DisplayName("관리자_계정을_저장한다")
    void saveAdminAccount() {
        // given
        User adminUser = User.createAdminUser(ADMIN_USER_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        // when
        userAdapter.saveAdminUser(adminUser);

        // then
        UserEntity saved = fakeJpaUserRepository.findByUserGuid(ADMIN_USER_GUID).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    @DisplayName("로그인을_시도하면_ID_값인_이메일로_AuthenticatedUser_를_조회한다")
    void getAuthenticatedUserByLoginId() {
        // given
        User adminUser = User.createAdminUser(ADMIN_USER_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);
        fakeJpaUserRepository.saveForSignup(UserMapper.toEntity(adminUser));

        // when
        AuthenticatedUser authenticatedUser = userAdapter.findAuthenticatedUserByEmail(ADMIN_EMAIL);

        // then
        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.email()).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    @DisplayName("새로운_사용자를_생성하면_사용자_정보를_저장한다")
    void saveUserWithPositionsAndSkills() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);

        // when
        User savedUser = userAdapter.save(user);

        // then
        assertThat(savedUser.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(savedUser.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(savedUser.getPassword()).isEqualTo(TEST_PASSWORD_1);
        assertThat(savedUser.getUsername()).isEqualTo(TEST_USERNAME_1);
    }

    @Test
    @DisplayName("사용자_식별키로_User_를_조회한다")
    void getUserByIdentifier() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeJpaUserRepository.save(UserMapper.toEntity(user));

        // when
        User foundUser = userAdapter.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("사용자_프로필_정보를_수정하면_변경된_값이_저장된다")
    void updateUserProfile() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        user.updateUsernameAndIntroduction(NEW_USERNAME, NEW_INTRO);

        // when
        userAdapter.updateUserProfile(user);

        // then
        User updatedUser = fakeJpaUserRepository.findByUserGuid(user.getUserGuid())
                .map(UserMapper::toDomain)
                .orElseThrow();
        assertThat(updatedUser.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(updatedUser.getIntroduction()).isEqualTo(NEW_INTRO);
    }


    @Test
    @DisplayName("회원탈퇴한_사용자의_deleted_값은_true_이다")
    void isDeletedUser() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeJpaUserRepository.save(UserMapper.toEntity(user));
        user.withdraw();

        // when
        userAdapter.delete(user);

        // then
        assertThat(fakeJpaUserRepository.findByUserGuid(user.getUserGuid())
                .orElseThrow()
                .isDeleted())
                .isTrue();
    }

    @Test
    @DisplayName("사용자_권한이_일치한다면_true_를_반환한다")
    void isUserRoleMatched() {
        // given
        User user = User.createAdminUser(ADMIN_USER_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);
        fakeJpaUserRepository.save(UserMapper.toEntity(user));

        // when
        boolean exists = userAdapter.existsByUserRole(UserRole.ADMIN);

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
        PageResult<User> pagedUserList = userAdapter.listUser(searchCommand, pageCommand.getPage(), pageCommand.getSize());

        // then
        assertThat(pagedUserList.content()).hasSize(2);
        assertThat(pagedUserList.content().get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(pagedUserList.content().get(1).getEmail()).isEqualTo("user2@example.com");
    }
}