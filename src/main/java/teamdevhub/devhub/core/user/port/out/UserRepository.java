package teamdevhub.devhub.core.user.port.out;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import teamdevhub.devhub.core.user.domain.User;
import teamdevhub.devhub.core.user.domain.vo.UserRole;
import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;
import teamdevhub.devhub.shared.enums.VerificationProvider;

public interface UserRepository {

    void saveAdminUser(User adminUser);
    User save(User user);
    AuthenticatedUser findAuthenticatedUserByEmail(String email);
    AuthenticatedUser findAuthenticatedUserByUserGuid(String userGuid);
    Optional<AuthenticatedUser> findOptionalByEmail(String email);
    Optional<AuthenticatedUser> findByOAuth(VerificationProvider verificationProvider, String oauthId);
    User findByUserGuid(String userGuid);
    void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime);
    void updateUserProfile(User user);
    void delete(User user);
    boolean existsByUserRole(UserRole userRole);
    Map<String, String> findNamesByUserGuid(List<String> userGuids);
}