package teamdevhub.devhub.fake.pure.usecase.auth;

import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.port.in.auth.AuthUserUseCase;

import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeAuthUserUseCase implements AuthUserUseCase {

    private final Map<String, User> store = new HashMap<>();

    public FakeAuthUserUseCase() {
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
    public void initializeAdminUser(String email, String rawPassword, String username) {
        User admin = User.createAdminUser(ADMIN_USER_GUID, email, rawPassword, username);
        store.put(admin.getUserGuid(), admin);
    }

    @Override
    public AuthenticatedUser getUserForLogin(String email) {
        return store.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst()
                .map(u -> new AuthenticatedUser(u.getUserGuid(), u.getEmail(), u.getPassword(), u.getUserRole()))
                .orElse(null);
    }

    @Override
    public AuthenticatedUser getUserForReissue(String userGuid) {
        return store.values().stream()
                .filter(u -> u.getUserGuid().equals(userGuid))
                .findFirst()
                .map(u -> new AuthenticatedUser(u.getUserGuid(), u.getEmail(), u.getPassword(), u.getUserRole()))
                .orElse(null);
    }
}
