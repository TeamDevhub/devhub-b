package teamdevhub.devhub.port.out.provider;

import org.springframework.security.core.Authentication;

public interface AuthenticatedUserProvider {

    Authentication authenticate(String email);
}
