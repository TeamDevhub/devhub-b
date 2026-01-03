package teamdevhub.devhub.port.in.user;

import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.domain.common.record.auth.AuthUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

public interface UserUseCase {
    User signup(SignupCommand signupCommand);
    AuthUser getAuthUser(String email);
    User getCurrentUserProfile(String userGuid);
    void initializeAdminUser(String email, String rawPassword, String username);
    boolean existsByUserRole(UserRole userRole);
    void updateLastLoginDateTime(String userGuid);
    void withdrawCurrentUser(String userGuid);
    void updateProfile(UpdateProfileCommand updateProfileCommand);
}