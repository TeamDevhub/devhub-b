package teamdevhub.devhub.fake.pure.usecase;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;
import teamdevhub.devhub.port.in.user.usecase.UserProfileUseCase;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static teamdevhub.devhub.constant.UserTestConstant.*;

public class FakeUserProfileUseCase implements UserProfileUseCase {

    private final Map<String, User> store = new HashMap<>();
    private final Set<String> updatedLoginUsers = new HashSet<>();

    public FakeUserProfileUseCase() {
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

    @Override
    public void updateLastLoginDateTime(String userGuid) {
        updatedLoginUsers.add(userGuid);
    }

    public boolean isLoginTimeUpdated(String userGuid) {
        return updatedLoginUsers.contains(userGuid);
    }

    @Override
    public User getCurrentUserProfile(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public void updateProfile(UpdateProfileCommand updateProfileCommand) {
        store.get(updateProfileCommand.getUserGuid());
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return store.values().stream().anyMatch(u -> u.getUserRole().equals(userRole));
    }
}
