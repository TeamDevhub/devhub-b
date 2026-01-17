package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.usecase.UserWithdrawUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserWithdrawUseCase implements UserWithdrawUseCase {

    private final Map<String, User> store = new HashMap<>();

    public FakeUserWithdrawUseCase() {
        User testUser = User.createGeneralUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1
        );
        store.put(TEST_USER_GUID_1, testUser);
    }

    @Override
    public void withdrawUser(String userGuid) {
        User user = store.get(userGuid);
        if (user != null) {
            user.withdraw();
        }
    }
}
