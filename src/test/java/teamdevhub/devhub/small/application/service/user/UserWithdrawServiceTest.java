package teamdevhub.devhub.small.application.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.user.UserWithdrawService;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.vo.user.UserCreateCommand;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.port.in.user.command.SignupCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserWithdrawServiceTest {

    private FakeUserRepository fakeUserRepository;

    private UserWithdrawService userWithdrawService;

    @BeforeEach
    void init() {
        FakeAuthenticationUseCase fakeAuthSessionUseCase = new FakeAuthenticationUseCase();
        fakeUserRepository = new FakeUserRepository();

        userWithdrawService = new UserWithdrawService(fakeAuthSessionUseCase, fakeUserRepository);
    }

    @Test
    @DisplayName("회원탈퇴한_사용자의_deleted_값은_true_이고_blocked_값은_false_이다")
    void setDeletedTrueWhenUserWithdraws() {
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
        UserCreateCommand generalUserCreateCommand = UserCreateCommand.generalUserCreateCommand(signupCommand, TEST_USER_GUID_1, TEST_PASSWORD_1);
        User testUser = User.createGeneralUser(generalUserCreateCommand);

        fakeUserRepository.save(testUser);

        // when
        userWithdrawService.withdrawUser(testUser.getUserGuid());

        // then
        assertThat(fakeUserRepository.findByUserGuid(testUser.getUserGuid()).isDeleted()).isTrue();
        assertThat(fakeUserRepository.findByUserGuid(testUser.getUserGuid()).isBlocked()).isFalse();
    }
}
