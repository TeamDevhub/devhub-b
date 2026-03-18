package teamdevhub.devhub.fake.pure.application.port.out.user;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.core.user.domain.vo.command.UpdateUserCommand;
import teamdevhub.devhub.core.user.port.out.UserRepository;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.VerificationProvider;

import java.time.LocalDateTime;
import java.util.*;

public class FakeUserRepository implements UserRepository {

    private final Map<String, User> store = new HashMap<>();
    private final List<String> calledMethods = new ArrayList<>();
    private final Map<String, LocalDateTime> lastLoginStore = new HashMap<>();

    @Override
    public void saveAdminUser(User adminUser) {
        store.put(adminUser.getUserGuid(), adminUser);
    }

    @Override
    public AuthenticatedUser findAuthenticatedUserByEmail(String email) {
        return store.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .map(user -> new AuthenticatedUser(user.getUserGuid(), user.getEmail(), user.getPassword(), user.getUserRole()))
                .orElse(null);
    }

    @Override
    public AuthenticatedUser findAuthenticatedUserByUserGuid(String userGuid) {
        return store.values().stream()
                .filter(user -> user.getUserGuid().equals(userGuid))
                .findFirst()
                .map(user -> new AuthenticatedUser(user.getUserGuid(), user.getEmail(), user.getPassword(), user.getUserRole()))
                .orElse(null);
    }

    @Override
    public Optional<AuthenticatedUser> findOptionalByEmail(String email) {
        return store.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .map(user -> new AuthenticatedUser(
                        user.getUserGuid(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getUserRole()
                ));
    }

    @Override
    public Optional<AuthenticatedUser> findByOAuth(VerificationProvider verificationProvider, String oauthId) {
        return store.values().stream()
                .filter(user ->
                        verificationProvider == user.getVerificationProvider() &&
                                oauthId.equals(user.getOauthId())
                )
                .findFirst()
                .map(user -> new AuthenticatedUser(
                        user.getUserGuid(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getUserRole()
                ));
    }

    @Override
    public User findByUserGuid(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public User save(User user) {
        calledMethods.add("saveTerms");
        store.put(user.getUserGuid(), user);
        return user;
    }

    @Override
    public void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime) {
        if (store.containsKey(userGuid)) {
            calledMethods.add("updateLastLoginDateTime");
            lastLoginStore.put(userGuid, lastLoginDateTime);
        }
    }

    @Override
    public void updateUserProfile(User user) {
        User existedUser = store.get(user.getUserGuid());
        if (existedUser != null) {
            UpdateUserCommand updateUserCommand = new UpdateUserCommand(user.getUsername(), user.getIntroduction());
            existedUser.updateBasicProfile(updateUserCommand);
        }
    }

    @Override
    public void delete(User user) {
        store.put(user.getUserGuid(), user);
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return store.values().stream().anyMatch(user -> user.getUserRole().equals(userRole));
    }

    @Override
    public Map<String, String> findNamesByUserGuid(List<String> userGuids) {
        return Map.of();
    }

    public boolean wasCalled(String methodName) {
        return calledMethods.contains(methodName);
    }

    public long callCount(String methodName) {
        return calledMethods.stream()
                .filter(methodName::equals)
                .count();
    }

    public LocalDateTime lastLoginOf(String userGuid) {
        return lastLoginStore.get(userGuid);
    }
}
