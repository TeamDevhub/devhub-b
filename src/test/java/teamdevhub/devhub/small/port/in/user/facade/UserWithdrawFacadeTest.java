package teamdevhub.devhub.small.port.in.user.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.fake.pure.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.usecase.user.FakeUserWithdrawUseCase;
import teamdevhub.devhub.core.user.port.in.facade.UserWithdrawFacade;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class UserWithdrawFacadeTest {

    private UserWithdrawFacade userWithdrawFacade;

    private FakeUserWithdrawUseCase userWithdrawUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;

    @BeforeEach
    void init() {
        userWithdrawUseCase = new FakeUserWithdrawUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();

        userWithdrawFacade = new UserWithdrawFacade(
                userWithdrawUseCase,
                authenticationUseCase
        );
    }

    @Test
    @DisplayName("withdrawUser_는_유저를_삭제_처리한다")
    void withdrawUserDeletesUser() {
        // given, when
        userWithdrawFacade.withdraw(TEST_USER_GUID_1);

        // then
        User deletedUser = userWithdrawUseCase.getUser(TEST_USER_GUID_1);
        assertThat(deletedUser.isDeleted()).isTrue();
    }
}
