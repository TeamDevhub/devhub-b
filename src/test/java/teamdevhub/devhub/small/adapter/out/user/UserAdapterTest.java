package teamdevhub.devhub.small.adapter.out.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import teamdevhub.devhub.adapter.in.admin.user.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.common.pagination.PageCommand;
import teamdevhub.devhub.adapter.out.user.UserAdapter;
import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserPositionEntity;
import teamdevhub.devhub.adapter.out.user.entity.UserSkillEntity;
import teamdevhub.devhub.adapter.out.user.mapper.UserMapper;
import teamdevhub.devhub.domain.common.record.auth.AuthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.record.UserPosition;
import teamdevhub.devhub.domain.user.record.UserSkill;
import teamdevhub.devhub.port.out.common.IdentifierProvider;
import teamdevhub.devhub.small.mock.persistence.FakeJpaUserPositionRepository;
import teamdevhub.devhub.small.mock.persistence.FakeJpaUserRepository;
import teamdevhub.devhub.small.mock.persistence.FakeJpaUserSkillRepository;
import teamdevhub.devhub.small.mock.persistence.FakeUserQueryRepository;
import teamdevhub.devhub.small.mock.provider.FakeUuidIdentifierProvider;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserAdapterTest {

    private static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    private static final String TEST_EMAIL = "user@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_USERNAME = "User";
    private static final String TEST_INTRO = "Hello World";
    private static final List<String> TEST_POSITION_LIST = List.of("001");
    private static final List<String> TEST_SKILL_LIST = List.of("001");
    private static final Set<UserPosition> TEST_POSITIONS = Set.of(new UserPosition("001"));
    private static final Set<UserSkill> TEST_SKILLS = Set.of(new UserSkill("001"));

    private static final String ADMIN_GUID = "ADMINa1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String ADMIN_EMAIL = "admin@example.com";
    private static final String ADMIN_USERNAME = "AdminUser";
    private static final String ADMIN_PASSWORD = "adminPassword123";

    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";
    private static final List<String> NEW_POSITION_LIST = List.of("002");
    private static final List<String> NEW_SKILL_LIST = List.of("002");
    private static final Set<UserPosition> NEW_POSITIONS = Set.of(new UserPosition("002"));
    private static final Set<UserSkill> NEW_SKILLS = Set.of(new UserSkill("002"));

    private UserAdapter userAdapter;
    private FakeJpaUserRepository fakeJpaUserRepository;
    private FakeUserQueryRepository fakeUserQueryRepository;
    private FakeJpaUserPositionRepository fakeJpaUserPositionRepository;
    private FakeJpaUserSkillRepository fakeJpaUserSkillRepository;
    private IdentifierProvider fakeIdentifierProvider;

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
    void saveAdminUser_savesUserEntity() {
        //given
        User adminUser = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);

        //when
        userAdapter.saveAdminUser(adminUser);

        //then
        UserEntity saved = fakeJpaUserRepository.findByUserGuid(ADMIN_GUID).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    void findUserByEmailForAuth_returnsAuthUser() {
        //given
        User adminUser = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);
        fakeJpaUserRepository.save(UserMapper.toEntity(adminUser));

        //when
        AuthUser authUser = userAdapter.findUserByEmailForAuth(ADMIN_EMAIL);

        //then
        assertThat(authUser).isNotNull();
        assertThat(authUser.email()).isEqualTo(ADMIN_EMAIL);
    }

    @Test
    void saveNewUser_insertsPositionsAndSkills() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);

        //when
        User savedUser = userAdapter.saveNewUser(user);

        //then
        assertThat(savedUser.getUserGuid()).isEqualTo(TEST_GUID);
        List<UserPositionEntity> positions = fakeJpaUserPositionRepository.findByUserGuid(TEST_GUID);
        assertThat(positions).hasSize(1);
        assertThat(positions.iterator().next().getPositionCd()).isEqualTo("001");
        List<UserSkillEntity> skills = fakeJpaUserSkillRepository.findByUserGuid(TEST_GUID);
        assertThat(skills).hasSize(1);
        assertThat(skills.iterator().next().getSkillCd()).isEqualTo("001");
    }

    @Test
    void findByUserGuid_returnsUserDomain() {
        //given
        User user = User.createGeneralUser(TEST_GUID, TEST_EMAIL, TEST_PASSWORD, TEST_USERNAME, TEST_INTRO, TEST_POSITION_LIST, TEST_SKILL_LIST);
        fakeJpaUserRepository.save(UserMapper.toEntity(user));
        fakeJpaUserPositionRepository.saveAll(Set.of(new UserPositionEntity(TEST_GUID + "-pos", TEST_GUID, "001")));
        fakeJpaUserSkillRepository.saveAll(Set.of(new UserSkillEntity(TEST_GUID + "-skill", TEST_GUID, "001")));

        //when
        User found = userAdapter.findByUserGuid(TEST_GUID);

        //then
        assertThat(found).isNotNull();
        assertThat(found.getUserGuid()).isEqualTo(TEST_GUID);
        assertThat(found.getPositions()).hasSize(1);
        assertThat(found.getSkills()).hasSize(1);
    }

    @Test
    void existsByUserRole_returnsTrueIfExists() {
        //given
        User user = User.createAdminUser(ADMIN_GUID, ADMIN_EMAIL, ADMIN_PASSWORD, ADMIN_USERNAME);
        fakeJpaUserRepository.save(UserMapper.toEntity(user));

        //when
        boolean exists = userAdapter.existsByUserRole(UserRole.ADMIN);

        //then
        assertThat(exists).isTrue();
    }

    @Test
    void listUser_returnsPagedAdminUserSummary() {
        //given
        PageCommand pageCommand = new PageCommand(0, 10);
        SearchUserCommand searchCommand = new SearchUserCommand(null, null, null, null);

        //when
        Page<AdminUserSummaryResponseDto> page = userAdapter.listUser(searchCommand, pageCommand);

        //then
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(page.getContent().get(1).getEmail()).isEqualTo("user2@example.com");
    }
}