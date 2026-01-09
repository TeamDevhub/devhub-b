package teamdevhub.devhub.small.mock.usecase;

import teamdevhub.devhub.port.in.user.command.SignupCommand;
import teamdevhub.devhub.port.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.UserPosition;
import teamdevhub.devhub.domain.user.vo.UserSkill;
import teamdevhub.devhub.port.in.user.UserUseCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FakeUserUseCase implements UserUseCase {

    private static final String TEST_GUID = "USERa1b2c3d4e5f6g7h8i9j10k11l12m";
    private static final String ADMIN_GUID = "ADMINa1b2c3d4e5f6g7h8i9j10k11l12";

    private static final String NEW_USERNAME = "NewUsername";
    private static final String NEW_INTRO = "NewIntro";
    private static final Set<UserPosition> NEW_POSITIONS = Set.of(new UserPosition("002"));
    private static final Set<UserSkill> NEW_SKILLS = Set.of(new UserSkill("002"));


    private final Map<String, User> store = new HashMap<>();

    public FakeUserUseCase() {
        User testUser = User.createGeneralUser(
                TEST_GUID,
                "user@example.com",
                "password123",
                "User",
                "Hello World",
                List.of("001"),
                List.of("001")
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
        User user = store.get(userGuid);
        user.updateProfile(NEW_USERNAME, NEW_INTRO, NEW_POSITIONS, NEW_SKILLS);
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
