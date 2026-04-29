package teamdevhub.devhub.small.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.common.audit.AuditInfo;
import teamdevhub.devhub.core.common.exception.DomainRuleException;
import teamdevhub.devhub.core.user.application.service.UserUserLoginService;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserLoginServiceTest {

    private UserUserLoginService userLoginService;

    private FakeUserRepository userRepository;
    private FakeTimeProvider timeProvider;

    @BeforeEach
    void init() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        userRepository = new FakeUserRepository();

        userLoginService = new UserUserLoginService(timeProvider, userRepository);
    }

    private User buildUser(String userGuid) {
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST).skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1).build();
        return User.createGeneralUser(CreateUserCommand.generalUserCreateCommand(signupUserCommand, userGuid));
    }

    private User buildBlockedUser(String userGuid) {
        return User.of(
                userGuid,
                UserRole.USER,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                null,
                36.5,
                true,
                null,
                false,
                AuditInfo.empty()
        );
    }

    @Test
    @DisplayName("차단된_유저가_로그인을_시도하면_예외가_발생한다")
    void validateLoginUser_blockedUser_throwsException() {
        // given
        User blockedUser = buildBlockedUser(TEST_USER_GUID_1);
        userRepository.save(blockedUser);

        // when, then
        assertThatThrownBy(() -> userLoginService.validateLoginUser(TEST_USER_GUID_1))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_BLOCKED.getMessage());
    }

    @Test
    @DisplayName("탈퇴한_유저가_로그인을_시도하면_예외가_발생한다")
    void validateLoginUser_withdrawnUser_throwsException() {
        // given
        User withdrawnUser = buildUser(TEST_USER_GUID_1);
        withdrawnUser.withdraw();
        userRepository.save(withdrawnUser);

        // when, then
        assertThatThrownBy(() -> userLoginService.validateLoginUser(TEST_USER_GUID_1))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_WITHDRAWN.getMessage());
    }

    @Test
    @DisplayName("로그인을_하면_최종_로그인_일시가_변한다")
    void updateLastLoginDateWhenLogin() {
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
        userRepository.save(testUser);

        // when
        userLoginService.updateLastLoginDateTime(TEST_USER_GUID_1);

        // then
        assertThat(userRepository.wasCalled("updateLastLoginDateTime")).isTrue();
        assertThat(userRepository.callCount("updateLastLoginDateTime")).isEqualTo(1);
        assertThat(userRepository.lastLoginOf(testUser.getUserGuid())).isEqualTo(timeProvider.now());
    }
}
