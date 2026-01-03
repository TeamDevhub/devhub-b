package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.domain.common.record.auth.AuthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;

public interface UserRepository {

    User saveNewUser(User user);
    AuthUser findAuthUserByEmail(String email);
    void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime);
    User findByUserGuid(String userGuid);
    void updateUserProfile(User user);
    boolean existsByUserRole(UserRole userRole);
}