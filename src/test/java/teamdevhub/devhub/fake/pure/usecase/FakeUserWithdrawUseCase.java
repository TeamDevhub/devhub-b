package teamdevhub.devhub.fake.pure.usecase;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.UserWithdrawUseCase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserWithdrawUseCase implements UserWithdrawUseCase {

    private final Map<String, User> store = new HashMap<>();
    private final Set<String> updatedLoginUsers = new HashSet<>();

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
