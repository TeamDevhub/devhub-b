package teamdevhub.devhub.core.user.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;

public interface UserRepository {

    void saveAdminUser(User adminUser);
    User save(User user);
    User findByUserGuid(String userGuid);
    void updateUserProfile(User user);
    void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime);
    void delete(User user);
    boolean existsByUserRole(UserRole userRole);
    Map<String, String> findNamesByUserGuid(List<String> userGuids);
}