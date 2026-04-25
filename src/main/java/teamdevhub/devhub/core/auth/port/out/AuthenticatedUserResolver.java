package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.vo.user.AuthenticatedUser;

public interface AuthenticatedUserResolver {

    AuthenticatedUser getAuthenticatedUser(String email, String rawPassword);
}
