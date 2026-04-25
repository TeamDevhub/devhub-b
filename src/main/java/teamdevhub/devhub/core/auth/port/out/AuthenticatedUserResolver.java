package teamdevhub.devhub.core.auth.port.out;

import teamdevhub.devhub.core.auth.domain.UserCredential;

public interface AuthenticatedUserResolver {

    UserCredential getAuthenticatedUser(String email, String rawPassword);
}
