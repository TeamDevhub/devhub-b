package teamdevhub.devhub.port.out.user;

import org.springframework.data.domain.Page;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

import java.time.LocalDateTime;

public interface UserRepository {

    void saveAdminUser(User adminUser);
    AuthenticatedUser findAuthenticatedUserByEmail(String email);
    AuthenticatedUser findAuthenticatedUserByUserGuid(String userGuid);
    User saveNewUser(User user);
    void updateLastLoginDateTime(String userGuid, LocalDateTime lastLoginDateTime);
    User findByUserGuid(String userGuid);
    void updateUserProfile(User user);
    void updateUserForWithdrawal(User user);
    boolean existsByUserRole(UserRole userRole);

    Page<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, int page, int size);
}