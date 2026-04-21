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
import teamdevhub.devhub.core.auth.domain.UserCredential;
import teamdevhub.devhub.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.outbound.user.adapter.UserAdapter;
import teamdevhub.devhub.outbound.user.adapter.entity.UserEntity;
import teamdevhub.devhub.outbound.user.adapter.mapper.UserMapper;
import teamdevhub.devhub.outbound.user.persistence.JpaUserRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.util.List;
import java.util.Optional;

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
        assertThat(saved.getEmail()).isEqualTo(adminUser.getEmail());
    }

    @Test
    @DisplayName("로그인을_시도하면_ID_값인_이메일로_AuthenticatedUser_를_조회한다")
    void getAuthenticatedUserByLoginId() {
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
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        jpaUserRepository.save(UserMapper.toEntity(testUser));

        // when
        UserCredential userCredential = userAdapter.findAuthenticatedUserByEmail(testUser.getEmail());

        // then
        assertThat(userCredential).isNotNull();
        assertThat(userCredential.loginId()).isEqualTo(testUser.getEmail());
    }

    @Test
    @DisplayName("userGuid_로 AuthenticatedUser_를 조회한다")
    void findAuthenticatedUserByUserGuid() {
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

        CreateUserCommand createCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User user = User.createGeneralUser(createCommand);

        jpaUserRepository.save(UserMapper.toEntity(user));

        // when
        UserCredential userCredential = userAdapter.findAuthenticatedUserByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(userCredential).isNotNull();
        assertThat(userCredential.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userCredential.loginId()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("존재하지 않는 userGuid_면 USER_NOT_FOUND 예외가 발생한다")
    void findAuthenticatedUserByUserGuid_notFound() {
        // expect
        assertThatThrownBy(
                () -> userAdapter.findAuthenticatedUserByUserGuid("NOT_EXIST_GUID"))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("이메일로 조회 시 Optional AuthenticatedUser_를 반환한다")
    void findOptionalByEmail_exists() {
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

        CreateUserCommand createCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User user = User.createGeneralUser(createCommand);

        jpaUserRepository.save(UserMapper.toEntity(user));

        // when
        Optional<UserCredential> result = userAdapter.findOptionalByEmail(TEST_EMAIL_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().loginId()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("OAuth_Provider_와 oauthId로 AuthenticatedUser_를 조회한다")
    void findByOAuth() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .userGuid(TEST_USER_GUID_1)
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .provider(VerificationProvider.GOOGLE)
                .oauthId("oauth-id-123")
                .userRole(UserRole.USER)
                .build();

        jpaUserRepository.save(userEntity);

        // when
        Optional<UserCredential> result =
                userAdapter.findByOAuth(VerificationProvider.GOOGLE, "oauth-id-123");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().loginId()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("존재하지 않는 OAuth 정보면 Optional.empty_를 반환한다")
    void findByOAuth_empty() {
        // when
        Optional<UserCredential> result =
                userAdapter.findByOAuth(VerificationProvider.GOOGLE, "not-exist-oauth-id");

        // then
        assertThat(result).isEmpty();
    }


    @Test
    @DisplayName("존재하지 않는 이메일이면 Optional.empty_를 반환한다")
    void findOptionalByEmail_empty() {
        // when
        Optional<UserCredential> result = userAdapter.findOptionalByEmail("not-exist@test.com");

        // then
        assertThat(result).isEmpty();
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
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

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
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
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
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
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
        CreateUserCommand generalCreateUserCommand = CreateUserCommand.generalUserCreateCommand(signupUserCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalCreateUserCommand);

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