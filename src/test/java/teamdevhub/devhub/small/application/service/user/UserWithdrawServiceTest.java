package teamdevhub.devhub.small.application.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.user.UserWithdrawService;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthSessionUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserWithdrawServiceTest {

    private FakeUserRepository fakeUserRepository;

    private UserWithdrawService userWithdrawService;

    @BeforeEach
    void init() {
        FakeAuthSessionUseCase fakeAuthSessionUseCase = new FakeAuthSessionUseCase();
        fakeUserRepository = new FakeUserRepository();

        userWithdrawService = new UserWithdrawService(fakeAuthSessionUseCase, fakeUserRepository);
    }

    @Test
    @DisplayName("회원탈퇴한_사용자의_deleted_값은_true_이고_blocked_값은_false_이다")
    void setDeletedTrueWhenUserWithdraws() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        userWithdrawService.withdrawUser(TEST_USER_GUID_1);

        // then
        assertThat(fakeUserRepository.findByUserGuid(TEST_USER_GUID_1).isDeleted()).isTrue();
        assertThat(fakeUserRepository.findByUserGuid(TEST_USER_GUID_1).isBlocked()).isFalse();
    }
}
