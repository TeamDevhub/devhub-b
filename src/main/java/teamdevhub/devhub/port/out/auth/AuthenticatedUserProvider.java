package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.domain.user.vo.AuthenticatedUser;

public interface AuthenticatedUserProvider {
    AuthenticatedUser getAuthenticatedUser(String email, String rawPassword);
}
