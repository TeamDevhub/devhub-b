package teamdevhub.devhub.small.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.adapter.out.user.UserAdapter;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.fake.pure.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.spring.persistence.user.FakeJpaUserPositionRepository;
import teamdevhub.devhub.fake.spring.persistence.user.FakeJpaUserRepository;
import teamdevhub.devhub.fake.spring.persistence.user.FakeJpaUserSkillRepository;
import teamdevhub.devhub.fake.spring.persistence.user.FakeUserQueryRepository;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.in.common.command.PageCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.TestConstant.*;

class UserAdapterTest {

    private UserAdapter userAdapter;
    private FakeJpaUserRepository fakeJpaUserRepository;
    private FakeJpaUserPositionRepository fakeJpaUserPositionRepository;
    private FakeJpaUserSkillRepository fakeJpaUserSkillRepository;
    private IdentifierProvider fakeIdentifierProvider;

    @BeforeEach
    void init() {
        fakeJpaUserRepository = new FakeJpaUserRepository();
        FakeUserQueryRepository fakeUserQueryRepository = new FakeUserQueryRepository();
        fakeJpaUserPositionRepository = new FakeJpaUserPositionRepository();
        fakeJpaUserSkillRepository = new FakeJpaUserSkillRepository();
        fakeIdentifierProvider = new FakeUuidIdentifierProvider(TEST_GUID_1);

        userAdapter = new UserAdapter(
                fakeJpaUserRepository,
                fakeUserQueryRepository
        );
    }

    @Test
    @DisplayName("관리자_계정을_저장한다")
    void saveAdminAccount() {
        // given
        User adminUser = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        // when
        userAdapter.saveAdminUser(adminUser);

        // then
        UserEntity saved = fakeJpaUserRepository.findByUserGuid(ADMIN_GUID).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    @DisplayName("로그인을_시도하면_ID_값인_이메일로_AuthenticatedUser_를_조회한다")
    void getAuthenticatedUserByLoginId() {
        // given
        User adminUser = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);
        fakeJpaUserRepository.saveForSignup(UserMapper.toEntity(adminUser));

        // when
        AuthenticatedUser authenticatedUser = userAdapter.findAuthenticatedUserByEmail(ADMIN_EMAIL);

        // then
        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.email()).isEqualTo(ADMIN_EMAIL);
    }
//
//    @Test
//    @DisplayName("새로운_사용자를_생성하면_사용자_관심_포지션과_사용자_보유_스킬을_저장한다")
//    void saveUserWithPositionsAndSkills() {
//        // given
//        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
//
//        // when
//        User savedUser = userAdapter.save(user);
//
//        // then
//        assertThat(savedUser.getUserGuid()).isEqualTo(TEST_GUID_1);
//        List<UserPositionEntity> positions = fakeJpaUserPositionRepository.findByUserGuid(TEST_GUID_1);
//        assertThat(positions).hasSize(1);
//        assertThat(positions.iterator().next().getPositionCd()).isEqualTo("001");
//        List<UserSkillEntity> skills = fakeJpaUserSkillRepository.findByUserGuid(TEST_GUID_1);
//        assertThat(skills).hasSize(1);
//        assertThat(skills.iterator().next().getSkillCd()).isEqualTo("001");
//    }
//
//    @Test
//    @DisplayName("사용자_식별키로_User_를_조회한다")
//    void getUserByIdentifier() {
//        // given
//        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
//        fakeJpaUserRepository.save(UserMapper.toEntity(user));
//        fakeJpaUserPositionRepository.saveAll(Set.of(new UserPositionEntity(TEST_GUID_1 + "-pos", TEST_GUID_1, "001")));
//        fakeJpaUserSkillRepository.saveAll(Set.of(new UserSkillEntity(TEST_GUID_1 + "-skill", TEST_GUID_1, "001")));
//
//        // when
//        User foundUser = userAdapter.findByUserGuidWithPositionsAndSkills(TEST_GUID_1);
//
//        // then
//        assertThat(foundUser).isNotNull();
//        assertThat(foundUser.getUserGuid()).isEqualTo(TEST_GUID_1);
//        assertThat(foundUser.getPositions()).hasSize(1);
//        assertThat(foundUser.getSkills()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("사용자_프로필_정보를_수정하면_변경된_값이_저장된다")
//    void updateUserProfile() {
//        // given
//        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
//        user.updateUsernameAndIntroduction(NEW_USERNAME, NEW_INTRO);
//
//        // when
//        userAdapter.updateUserProfile(user);
//
//        // then
//        User updatedUser = fakeJpaUserRepository.findByUserGuid(user.getUserGuid())
//                .map(UserMapper::toDomain)
//                .orElseThrow();
//        assertThat(updatedUser.getUsername()).isEqualTo(NEW_USERNAME);
//        assertThat(updatedUser.getIntroduction()).isEqualTo(NEW_INTRO);
//        assertThat(updatedUser.getPositions()).isEqualTo(NEW_POSITIONS);
//    }
//
//    @Test
//    @DisplayName("사용자_프로필_정보_일부_포지션만_변경하면_지정한_방식에_맞게_수정된다")
//    void updateUserProfilePositions() {
//        // given
//        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
//
//        // when
//        fakeJpaUserRepository.save(UserMapper.toEntity(user));
//        fakeJpaUserPositionRepository.saveAll(
//                user.getPositions().stream()
//                        .map(userPosition -> UserPositionEntity.builder()
//                                .userGuid(TEST_GUID_1)
//                                .userPositionGuid(fakeIdentifierProvider.generateIdentifier())
//                                .positionCd(userPosition.positionCode())
//                                .build())
//                        .toList()
//        );
//
//        user.changePositions(NEW_POSITIONS);
//
//        userAdapter.updateUserProfile(user);
//
//        // then
//        List<UserPositionEntity> positionsAfterUpdate = fakeJpaUserPositionRepository.findByUserGuid(TEST_GUID_1);
//
//        assertThat(positionsAfterUpdate)
//                .hasSize(1)
//                .extracting(UserPositionEntity::getPositionCd)
//                .containsExactly("001");
//    }

    @Test
    @DisplayName("회원탈퇴한_사용자의_deleted_값은_true_이다")
    void isDeletedUser() {
        // given
        User user = User.createGeneralUser(TEST_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
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
        User user = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);
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
        PageResult<AdminUserSummaryResponseDto> page = userAdapter.listUser(searchCommand, pageCommand.getPage(), pageCommand.getSize());

        // then
        assertThat(page.content()).hasSize(2);
        assertThat(page.content().get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(page.content().get(1).getEmail()).isEqualTo("user2@example.com");
    }
}