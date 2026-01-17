package teamdevhub.devhub.fake.pure.usecase.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.user.usecase.UserLoginUseCase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserLoginUseCase implements UserLoginUseCase {

    private final Map<String, User> store = new HashMap<>();
    private final Set<String> updatedLoginUsers = new HashSet<>();

    public FakeUserLoginUseCase() {
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
    public void updateLastLoginDateTime(String userGuid) {
        updatedLoginUsers.add(userGuid);
    }

    public boolean isLoginTimeUpdated(String userGuid) {
        return updatedLoginUsers.contains(userGuid);
    }

}

