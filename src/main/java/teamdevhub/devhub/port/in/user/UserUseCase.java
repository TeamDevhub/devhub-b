package teamdevhub.devhub.port.in.user;

import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.domain.record.auth.LoginUser;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

public interface UserUseCase {
    User signup(SignupCommand signupCommand);
    LoginUser getLoginUser(String email);
    User getCurrentUserProfile(String userGuid);
    void initializeAdminUser(String email, String rawPassword, String username);
    boolean existsByUserRole(UserRole userRole);
    void updateLastLoginDateTime(String userGuid);
    void withdrawCurrentUser(String userGuid);
    void updateProfile(UpdateProfileCommand updateProfileCommand);
}