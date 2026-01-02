package teamdevhub.devhub.port.in.user;

import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.adapter.in.user.command.UpdateProfileCommand;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

public interface UserUseCase {
    User signup(SignupCommand signupCommand);
    void initializeAdminUser(String email, String username, String rawPassword);
    boolean existsByUserRole(UserRole userRole);
    void updateLastLoginDate(String userGuid);
    void withdrawCurrentUser(String userGuid);
    void updateProfile(UpdateProfileCommand updateProfileCommand);
}