package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.outbound.auth.infrastructure.security.vo.AuthenticatedUser;

public interface AuthenticatedUserResolver {

    AuthenticatedUser getAuthenticatedUser(String email, String rawPassword);
}
