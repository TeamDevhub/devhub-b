package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.util.Optional;

public interface UserRepository {

    User save(User user);
    User findByUserGuid(String userGuid);
    User findByEmail(String email);
    boolean existsByUserRole(UserRole userRole);
}