package teamdevhub.devhub.port.in.user;

import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.domain.common.record.auth.LoginUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

public interface UserUseCase {
    void initializeAdminUser(String email, String rawPassword, String username);
    LoginUser getUserForAuth(String email);
    User signup(SignupCommand signupCommand);
    void updateLastLoginDateTime(String userGuid);
    User getCurrentUserProfile(String userGuid);
    void updateProfile(UpdateProfileCommand updateProfileCommand);
    void withdrawCurrentUser(String userGuid);
    boolean existsByUserRole(UserRole userRole);
}