package teamdevhub.devhub.small.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.port.in.common.command.PageCommand;
import teamdevhub.devhub.adapter.out.user.UserAdapter;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.common.provider.uuid.IdentifierProvider;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.small.common.mock.persistence.FakeJpaUserPositionRepository;
import teamdevhub.devhub.small.common.mock.persistence.FakeJpaUserRepository;
import teamdevhub.devhub.small.common.mock.persistence.FakeJpaUserSkillRepository;
import teamdevhub.devhub.small.common.mock.persistence.FakeUserQueryRepository;
import teamdevhub.devhub.small.common.mock.provider.FakeDateTimeProvider;
import teamdevhub.devhub.small.common.mock.provider.FakeUuidIdentifierProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.small.common.mock.constant.TestConstant.*;

class UserAdapterTest {

    private UserAdapter userAdapter;
    private FakeJpaUserRepository fakeJpaUserRepository;
    private FakeUserQueryRepository fakeUserQueryRepository;
    private FakeJpaUserPositionRepository fakeJpaUserPositionRepository;
    private FakeJpaUserSkillRepository fakeJpaUserSkillRepository;
    private IdentifierProvider fakeIdentifierProvider;
    private final FakeDateTimeProvider fakeDateTimeProvider = new FakeDateTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));

    @BeforeEach
    void init() {
        fakeJpaUserRepository = new FakeJpaUserRepository();
        fakeUserQueryRepository = new FakeUserQueryRepository();
        fakeJpaUserPositionRepository = new FakeJpaUserPositionRepository();
        fakeJpaUserSkillRepository = new FakeJpaUserSkillRepository();
        fakeIdentifierProvider = new FakeUuidIdentifierProvider(TEST_GUID);

        userAdapter = new UserAdapter(
                fakeJpaUserRepository,
                fakeUserQueryRepository,
                fakeJpaUserPositionRepository,
                fakeJpaUserSkillRepository,
                fakeIdentifierProvider
        );
    }

    @Test
    void 관리자_계정을_데이터베이스에_저장한다() {
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
    void 로그인을_시도하면_ID_값인_이메일로_AuthenticatedUser_를_조회한다() {
        // given
        User adminUser = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);
        fakeJpaUserRepository.saveForSignup(UserMapper.toEntity(adminUser));

        // when
        AuthenticatedUser authenticatedUser = userAdapter.findByEmailForLogin(ADMIN_EMAIL);

        // then
        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.email()).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    void 새로운_사용자를_생성하면_사용자_관심_포지션과_사용자_보유_스킬을_저장한다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        User savedUser = userAdapter.saveNewUser(user);

        // then
        assertThat(savedUser.getUserGuid()).isEqualTo(TEST_GUID);
        List<UserPositionEntity> positions = fakeJpaUserPositionRepository.findByUserGuid(TEST_GUID);
        assertThat(positions).hasSize(1);
        assertThat(positions.iterator().next().getPositionCd()).isEqualTo("001");
        List<UserSkillEntity> skills = fakeJpaUserSkillRepository.findByUserGuid(TEST_GUID);
        assertThat(skills).hasSize(1);
        assertThat(skills.iterator().next().getSkillCd()).isEqualTo("001");
    }

    @Test
    void 사용자가_로그인을_하면_최종_로그인_시간이_변경된다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        fakeJpaUserRepository.save(UserMapper.toEntity(user));
        // when
        userAdapter.updateLastLoginDateTime(TEST_GUID,fakeDateTimeProvider.now());

        // then
        assertThat(fakeJpaUserRepository.findByUserGuid(user.getUserGuid())
                .orElseThrow()
                .getLastLoginDt())
                .isEqualTo(fakeDateTimeProvider.now());
    }

    @Test
    void 사용자_식별키로_User_를_조회한다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        fakeJpaUserRepository.save(UserMapper.toEntity(user));
        fakeJpaUserPositionRepository.saveAll(Set.of(new UserPositionEntity(TEST_GUID + "-pos", TEST_GUID, "001")));
        fakeJpaUserSkillRepository.saveAll(Set.of(new UserSkillEntity(TEST_GUID + "-skill", TEST_GUID, "001")));

        // when
        User found = userAdapter.findByUserGuid(TEST_GUID);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(found.getPositions()).hasSize(1);
        assertThat(found.getSkills()).hasSize(1);
    }

    @Test
    void 사용자_프로필_정보를_수정하면_변경된_값이_저장된다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        user.updateProfile(NEW_USERNAME, NEW_INTRO, NEW_POSITIONS, NEW_SKILLS);

        // when
        userAdapter.updateUserProfile(user);

        // then
        User updatedUser = fakeJpaUserRepository.findByUserGuid(user.getUserGuid())
                .map(userEntity -> UserMapper.toDomain(userEntity, NEW_POSITIONS, NEW_SKILLS))
                .orElseThrow();
        assertThat(updatedUser.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(updatedUser.getIntroduction()).isEqualTo(NEW_INTRO);
        assertThat(updatedUser.getPositions()).isEqualTo(NEW_POSITIONS);
    }

    @Test
    void 사용자_프로필_정보_일부_포지션만_변경하면_diff_전략에_맞게_처리된다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        // when
        fakeJpaUserRepository.save(UserMapper.toEntity(user));
        fakeJpaUserPositionRepository.saveAll(
                user.getPositions().stream()
                        .map(userPosition -> UserPositionEntity.builder()
                                .userGuid(TEST_GUID)
                                .userInterestPositionGuid(fakeIdentifierProvider.generateIdentifier())
                                .positionCd(userPosition.positionCode())
                                .build())
                        .toList()
        );

        fakeJpaUserSkillRepository.saveAll(
                user.getSkills().stream()
                        .map(userSkill -> UserSkillEntity.builder()
                                .userGuid(TEST_GUID)
                                .userSkillGuid(fakeIdentifierProvider.generateIdentifier())
                                .skillCd(userSkill.skillCode())
                                .build())
                        .toList()
        );

        user.updateProfile(NEW_USERNAME, NEW_INTRO, TEST_POSITIONS, NEW_SKILLS);


        userAdapter.updateUserProfile(user);

        // then
        Set<String> positionsAfterUpdate = fakeJpaUserPositionRepository.findByUserGuid(TEST_GUID)
                .stream().map(UserPositionEntity::getPositionCd).collect(Collectors.toSet());
        Set<String> skillsAfterUpdate = fakeJpaUserSkillRepository.findByUserGuid(TEST_GUID)
                .stream().map(UserSkillEntity::getSkillCd).collect(Collectors.toSet());

        assertThat(positionsAfterUpdate).containsExactlyInAnyOrder("001");
        assertThat(skillsAfterUpdate).containsExactlyInAnyOrder("002");
    }

    @Test
    void 회원탈퇴한_사용자의_deleted_값은_true_이다() {
        // given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        user.withdraw();

        // when
        userAdapter.updateUserForWithdrawal(user);

        // then
        assertThat(fakeJpaUserRepository.findByUserGuid(user.getUserGuid())
                .orElseThrow()
                .isDeleted())
                .isTrue();
    }

    @Test
    void 사용자_권한이_일치한다면_true_를_반환한다() {
        // given
        User user = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);
        fakeJpaUserRepository.save(UserMapper.toEntity(user));

        // when
        boolean exists = userAdapter.existsByUserRole(UserRole.ADMIN);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 사용자_목록_조회를_하면_AdminUserSummaryResponseDto_로_된_Page_데이터를_반환한다() {
        // given
        PageCommand pageCommand = new PageCommand(0, 10);
        SearchUserCommand searchCommand = new SearchUserCommand(null, null, null, null);

        // when
        Page<AdminUserSummaryResponseDto> page = userAdapter.listUser(searchCommand, pageCommand.getPage(), pageCommand.getSize());

        // then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(page.getContent().get(1).getEmail()).isEqualTo("user2@example.com");
    }
}