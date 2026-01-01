package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.util.Optional;

public interface UserPort {

    User save(User user);
    Optional<User> findByUserGuid(String userGuid);
    Optional<User> findByEmail(String email);
    boolean existsByUserRole(UserRole userRole);
}