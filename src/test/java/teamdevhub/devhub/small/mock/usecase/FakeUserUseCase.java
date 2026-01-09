package teamdevhub.devhub.small.mock.usecase;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.port.in.user.UserUseCase;
import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static teamdevhub.devhub.small.mock.constant.TestConstant.*;

public class FakeUserUseCase implements UserUseCase {

    private final Map<String, User> store = new HashMap<>();
    private final Set<String> updatedLoginUsers = new HashSet<>();

    public FakeUserUseCase() {
        User testUser = User.createGeneralUser(
                TEST_GUID,
                TEST_EMAIL,
                TEST_PASSWORD,
                TEST_USERNAME,
                TEST_INTRO,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST
        );
        store.put(TEST_GUID, testUser);
    }

    @Override
    public void initializeAdminUser(String email, String rawPassword, String username) {
        User admin = User.createAdminUser(ADMIN_GUID, email, rawPassword, username);
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
    public User signup(SignupCommand signupCommand) {
        User user = User.createGeneralUser(
                TEST_GUID,
                signupCommand.getEmail(),
                signupCommand.getPassword(),
                signupCommand.getUsername(),
                signupCommand.getIntroduction(),
                signupCommand.getPositionList(),
                signupCommand.getSkillList()
        );
        store.put(user.getUserGuid(), user);
        return user;
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
    public void withdrawCurrentUser(String userGuid) {
        User user = store.get(userGuid);
        if (user != null) {
            user.withdraw();
        }
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return store.values().stream().anyMatch(u -> u.getUserRole().equals(userRole));
    }
}
