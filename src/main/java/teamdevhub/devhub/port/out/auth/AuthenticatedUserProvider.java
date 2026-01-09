package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;

public interface AuthenticatedUserProvider {
    AuthenticatedUser getAuthenticatedUser(String email, String rawPassword);
}
