package teamdevhub.devhub.small.application.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.application.service.user.UserLoginService;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.fake.pure.provider.FakeTimeProvider;
import teamdevhub.devhub.fake.pure.repository.user.FakeUserRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserLoginServiceTest {

    private UserLoginService userLoginService;

    private FakeUserRepository fakeUserRepository;
    private FakeTimeProvider fakeDateTimeProvider;

    @BeforeEach
    void init() {
        fakeUserRepository = new FakeUserRepository();
        fakeDateTimeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));

        userLoginService = new UserLoginService(fakeUserRepository, fakeDateTimeProvider);
    }

    @Test
    @DisplayName("로그인을_하면_최종_로그인_일시가_변한다")
    void updateLastLoginDateWhenLogin() {
        // given
        User user = User.createGeneralUser(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, TEST_USERNAME_1, TEST_INTRO_1);
        fakeUserRepository.save(user);

        // when
        userLoginService.updateLastLoginDateTime(TEST_USER_GUID_1);

        // then
        assertThat(fakeUserRepository.wasCalled("updateLastLoginDateTime")).isTrue();
        assertThat(fakeUserRepository.callCount("updateLastLoginDateTime")).isEqualTo(1);
        assertThat(fakeUserRepository.lastLoginOf(user.getUserGuid())).isEqualTo(fakeDateTimeProvider.now());
    }
}
