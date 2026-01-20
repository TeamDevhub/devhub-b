package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.adapter.in.common.vo.PageResult;
import teamdevhub.devhub.common.enums.VerificationProvider;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.auth.vo.user.AuthenticatedUser;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;

import java.time.LocalDateTime;
import java.util.Optional;

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

    PageResult<User> listUser(SearchUserCommand searchUserCommand, int page, int size);
}