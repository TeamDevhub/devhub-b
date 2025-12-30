package teamdevhub.devhub.port.in.user;

import teamdevhub.devhub.adapter.in.user.command.SignupCommand;
import teamdevhub.devhub.domain.user.User;
import teamdevhub.devhub.domain.user.UserRole;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserUseCase {
    User signup(SignupCommand signupCommand);
    void createAdminUser(String email, String username, String rawPassword);
    boolean existsByRole(UserRole role);
    @PreAuthorize("@userAuthChecker.isSelf(#userGuid)")
    void update();
}