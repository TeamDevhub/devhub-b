package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.domain.auth.vo.AuthenticatedUser;

public interface AuthenticatedUserResolver {
    AuthenticatedUser getAuthenticatedUser(String email, String rawPassword);
}
