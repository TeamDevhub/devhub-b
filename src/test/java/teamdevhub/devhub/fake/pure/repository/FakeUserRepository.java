package teamdevhub.devhub.fake.pure.repository;

import teamdevhub.devhub.adapter.in.admin.user.dto.UserBasicResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.user.vo.AuthenticatedUser;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.port.out.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public User save(User user) {
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
    public User findByUserGuid(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public void updateUserProfile(User user) {
        User existedUser = store.get(user.getUserGuid());
        if (existedUser != null) {
            existedUser.updateUsernameAndIntroduction(
                    user.getUsername(),
                    user.getIntroduction()
            );
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
    public PageResult<User> listUser(SearchUserCommand searchUserCommand, int page, int size) {
        List<User> userList = new ArrayList<>(store.values());

        long totalElements = userList.size();
        int start = page * size;
        int end = Math.min(start + size, userList.size());
        List<User> pageContent = start >= end ? Collections.emptyList() : userList.subList(start, end);

        return PageResult.of(
                pageContent,
                page,
                size,
                totalElements
        );
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
