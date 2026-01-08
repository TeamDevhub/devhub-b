package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.domain.common.record.auth.AuthenticatedUser;

public interface Authenticator {
    AuthenticatedUser authenticate(String email, String rawPassword);
}
