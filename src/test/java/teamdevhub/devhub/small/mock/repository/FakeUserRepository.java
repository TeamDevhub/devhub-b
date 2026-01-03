package teamdevhub.devhub.small.mock.repository;

import teamdevhub.devhub.common.enums.ErrorCodeEnum;
import teamdevhub.devhub.common.exception.BusinessRuleException;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.user.UserRepository;

import java.util.HashMap;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    private final HashMap<String, User> store = new HashMap<>();

    @Override
    public User save(User user) {
        store.put(user.getUserGuid(), user);
        return user;
    }

    @Override
    public User findByUserGuid(String userGuid) {
        return store.get(userGuid);
    }

    @Override
    public User findByEmail(String email) {
        return store.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElseThrow(() -> BusinessRuleException.of(ErrorCodeEnum.USER_NOT_FOUND));
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return store.values().stream().anyMatch(user -> user.getUserRole().equals(userRole));
    }
}
