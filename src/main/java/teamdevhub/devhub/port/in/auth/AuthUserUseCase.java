package teamdevhub.devhub.port.in.auth;

import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;

public interface AuthUserUseCase {
    void initializeAdminUser(String email, String rawPassword, String username);
    AuthenticatedUser getUserForLogin(String email);
    AuthenticatedUser getUserForReissue(String userGuid);
}
