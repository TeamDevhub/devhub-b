package teamdevhub.devhub.port.out.user;

import teamdevhub.devhub.adapter.in.admin.user.dto.AdminUserSummaryResponseDto;
import teamdevhub.devhub.adapter.in.vo.PageResult;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;
import teamdevhub.devhub.port.in.admin.command.SearchUserCommand;

public interface UserRepository {

    void saveAdminUser(User adminUser);
    AuthenticatedUser findAuthenticatedUserByEmail(String email);
    AuthenticatedUser findAuthenticatedUserByUserGuid(String userGuid);
    User save(User user);
    void updateLastLoginDateTime(User user);
    User findByUserGuid(String userGuid);
    User findByUserGuidWithPositionsAndSkills(String userGuid);
    void updateUserProfile(User user);
    void delete(User user);
    boolean existsByUserRole(UserRole userRole);
    PageResult<AdminUserSummaryResponseDto> listUser(SearchUserCommand searchUserCommand, int page, int size);
}