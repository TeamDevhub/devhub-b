package teamdevhub.devhub.port.in.user;

import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;

public interface UserUseCase {
    User signup(SignupCommand signupCommand);
    void initializeAdminUser(String email, String username, String rawPassword);
    boolean existsByRole(UserRole role);
    void updateLastLoginDate(String userGuid);
}