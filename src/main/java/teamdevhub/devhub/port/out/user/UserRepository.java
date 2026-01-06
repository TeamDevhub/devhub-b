package teamdevhub.devhub.port.out.user;

import org.springframework.data.domain.Page;
import teamdevhub.devhub.adapter.in.admin.user.command.SearchUserCommand;
import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.common.pagination.PageCommand;
import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

public interface UserRepository {

    void saveAdminUser(User adminUser);
    AuthenticatedUser findUserByEmailForAuth(String email);
    User saveNewUser(User user);
    void updateLastLoginDateTime(User user);
    User findByUserGuid(String userGuid);
    void updateUserProfile(User user);
    void updateUserForWithdrawal(User user);
    boolean existsByUserRole(UserRole userRole);

    Page<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, int page, int size);
}