package teamdevhub.devhub.adapter.out.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.port.out.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {

    private final UserRepositoryJpa userRepositoryJpa;

    @Override
    public User save(User user) {
        return UserMapper.toDomain(userRepositoryJpa.save(UserMapper.toEntity(user)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepositoryJpa.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryJpa.findByEmail(email)
                .map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByRole(UserRole role) {
        return userRepositoryJpa.existsByRole(role);
    }
}