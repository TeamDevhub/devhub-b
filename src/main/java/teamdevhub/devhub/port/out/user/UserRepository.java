package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.domain.record.auth.LoginUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;

public interface UserRepository {

    User saveNewUser(User user);
    LoginUser findForLoginByEmail(String email);
    void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime);
    User findByUserGuid(String userGuid);
    User findByEmail(String email);
    boolean existsByUserRole(UserRole userRole);
}