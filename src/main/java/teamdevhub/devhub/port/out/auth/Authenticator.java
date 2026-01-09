package teamdevhub.devhub.port.out.auth;

import teamdevhub.devhub.domain.vo.auth.AuthenticatedUser;

public interface Authenticator {
    AuthenticatedUser authenticate(String email, String rawPassword);
}
