package teamdevhub.devhub.adapter.out.user;

import teamdevhub.devhub.adapter.out.user.entity.UserEntity;
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
        UserEntity userEntity = userRepositoryJpa.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(userEntity);
    }

    @Override
    public Optional<User> findByUserGuid(String userGuid) {
        return userRepositoryJpa.findByUserGuid(userGuid).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositoryJpa.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByUserRole(UserRole userRole) {
        return userRepositoryJpa.existsByUserRole(userRole);
    }
}